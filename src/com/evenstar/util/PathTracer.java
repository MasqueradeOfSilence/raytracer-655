package com.evenstar.util;

import com.evenstar.model.PPMImage;
import com.evenstar.model.Ray;
import com.evenstar.model.Scene;
import com.evenstar.model.math.CoordinateSystem;
import com.evenstar.model.physics.Hit;
import com.evenstar.model.shapes.Shape;
import com.evenstar.model.shapes.Sphere;
import com.evenstar.model.shapes.Triangle;
import com.evenstar.model.textures.*;
import com.evenstar.model.vectors.*;
import com.evenstar.util.physics.*;
import org.apache.commons.math3.distribution.UniformRealDistribution;

import java.util.ArrayList;

public class PathTracer
{
    private final Raytracer raytracer;
    private final PPMRenderer ppmRenderer;
    private final Scene scene;
    private final Intersector intersector;
    private final Lighter lighter;
    private final Shadower shadower;
    private final Reflector reflector;
    private final Refractor refractor;
    private final Texturer texturer;

    public PathTracer(Scene scene)
    {
        this.raytracer = new Raytracer(scene);
        this.ppmRenderer = new PPMRenderer();
        this.scene = scene;
        this.intersector = new Intersector(scene);
        this.lighter = new Lighter();
        this.shadower = new Shadower(scene);
        this.reflector = new Reflector();
        this.refractor = new Refractor();
        this.texturer = new Texturer();
    }

    private String getFileName(String fileName)
    {
        fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
        return "output/" + fileName.split("\\.")[0] + ".ppm";
    }

    private Vector3D uniformSampleHemisphere(double r1, double r2)
    {
        double sinTheta = Math.sqrt(1 - r1 * r1);
        double phi = 2 * Math.PI * r2;
        double x = sinTheta * Math.cos(phi);
        double z = sinTheta * Math.sin(phi);
        return new Vector3D(x, r1, z);
    }

    private CoordinateSystem createCoordinateSystem(SphereNormal N)
    {
        SphereNormal NT;
        if (Math.abs(N.getVector().getX()) > Math.abs(N.getVector().getY()))
        {
            NT = new SphereNormal(VectorOperations.divideByScalar(new Vector3D(N.getVector().getZ(), 0, -N.getVector().getX()),
                    Math.sqrt(Math.pow(N.getVector().getX(), 2) + Math.pow(N.getVector().getZ(), 2))));
        }
        else
        {
            NT = new SphereNormal(VectorOperations.divideByScalar(new Vector3D(0, -N.getVector().getZ(), -N.getVector().getY()),
                    Math.sqrt(Math.pow(N.getVector().getY(), 2) + Math.pow(N.getVector().getZ(), 2))));
        }
        SphereNormal Nb = new SphereNormal(VectorOperations.crossProduct(N.getVector(), NT.getVector()));
        return new CoordinateSystem(N, NT, Nb);
    }

    public Color colorShape(Hit hit, Ray ray, int depth, int i, int j)
    {
        if (depth == 2)
        {
            return new Color(0, 0, 0);
        }
        int n = 1;
        Shape shape = hit.getCorrespondingShape();
        Color hitColor = new Color(1, 1, 1);
        Color directLighting;
        if (ClassIdentifier.isSphere(shape))
        {
            Sphere sphere = (Sphere) shape;
            SphereNormal sphereNormal = new SphereNormal(hit.getHitPoint(), sphere.getCenter());
            if (ClassIdentifier.isDiffuse(sphere.getMaterial()))
            {
                if (this.shadower.isInShadow(this.scene, hit))
                {
                    // Tint the ambient light
                    Vector3D combined = VectorOperations.multiplyVectors(sphere.getMaterial().getVector(),
                            scene.getAmbientLight().getLightColor().getVector());
                    return new Color(combined);
                }
                directLighting = this.lighter.getFinalColor(new Color(sphere.getMaterial().getVector()),
                        sphereNormal.getVector(), this.scene.getDirectionalLight(), (Diffuse)sphere.getMaterial(),
                        hit.getHitPoint().getVector(), ray, this.scene, n, Constants.DEFAULT_COEFFICIENT, i, j);
                //return directLighting;
                // This is where we do the fancy stuff
                int numberOfSamples = 128;
                CoordinateSystem coordinateSystem = this.createCoordinateSystem(sphereNormal);
                double pdf = 1 / (2 * Math.PI);
                UniformRealDistribution ur = new UniformRealDistribution(0, 1);
                Vector3D indirectLighting = new Vector3D(0, 0, 0);
                for (int i1 = 0; i1 < numberOfSamples; i1++)
                {
                    double r1 = ur.sample();
                    double r2 = ur.sample();
                    Vector3D sample = this.uniformSampleHemisphere(r1, r2);
                    Vector3D sampleWorld = new Vector3D(
                            sample.getX() * coordinateSystem.getNb().getVector().getX() +
                            sample.getY() * sphereNormal.getVector().getX() +
                            sample.getZ() * coordinateSystem.getNT().getVector().getX(),

                            sample.getX() * coordinateSystem.getNb().getVector().getY() +
                            sample.getY() * sphereNormal.getVector().getY() +
                            sample.getZ() + coordinateSystem.getNT().getVector().getY(),

                            sample.getX() * coordinateSystem.getNb().getVector().getZ() +
                            sample.getY() * sphereNormal.getVector().getZ() +
                            sample.getZ() * coordinateSystem.getNT().getVector().getZ()
                    );
                    Ray ray2 = new Ray(new Point(VectorOperations.addVectors(hit.getHitPoint().getVector(),
                            VectorOperations.multiplyByScalar(sampleWorld, 0.0001))), new Direction(sampleWorld));
                    ArrayList<Hit> rayShapeHits = this.intersector.computeRayShapeHits(ray2, this.scene.getShapes(), this.scene);
                    if (this.raytracer.nothingHit(rayShapeHits))
                    {
                        return directLighting;
                    }
                    Hit closest = this.raytracer.getClosestHit(rayShapeHits);
                    indirectLighting = VectorOperations.addVectors(indirectLighting,
                            VectorOperations.multiplyByScalar(this.colorShape(
                                closest, ray2, depth, i, j
                            ).getVector(), r1));
                    indirectLighting = VectorOperations.divideByScalar(indirectLighting, pdf);
                }
                indirectLighting = VectorOperations.divideVectors(indirectLighting, sphereNormal.getVector());
                Vector3D hitColorVector;
                Vector3D sum = VectorOperations.addVectors(directLighting.getVector(), indirectLighting);
                Vector3D product = VectorOperations.multiplyVectors(sum, ((Diffuse)sphere.getMaterial()).getVector());
                Vector3D minuend = VectorOperations.divideByScalar(product, Math.PI);
                hitColorVector = minuend;
                hitColor = new Color(hitColorVector);
                return hitColor;
            }
            else if (ClassIdentifier.isReflective(sphere.getMaterial()))
            {
                return this.reflector.getReflectionColor(ray, sphereNormal, hit.getHitPoint(), sphere, this.scene,
                        this.intersector, this.raytracer, i, j);
            }
            else if (ClassIdentifier.isGlass(sphere.getMaterial()))
            {
                Glass glass = (Glass) sphere.getMaterial();
                return this.refractor.getReflectedAndRefractedColor(ray, sphereNormal, glass, hit.getHitPoint(),
                        this.intersector, this.scene, this.raytracer, sphere, this.reflector, i, j);
            }
            // Amber to test translucent materials
            else if (ClassIdentifier.isAmber(sphere.getMaterial()))
            {
                Amber amber = (Amber) sphere.getMaterial();
                return this.refractor.getReflectedAndRefractedColor(ray, sphereNormal, amber, hit.getHitPoint(),
                        this.intersector, this.scene, this.raytracer, sphere, this.reflector, i, j);
            }
            // Glossy materials
            else if (ClassIdentifier.isPhong(sphere.getMaterial()))
            {
                // can change n based on desired appearance
                Phong phong = (Phong) sphere.getMaterial();
                n = phong.getN();
                if (this.shadower.isInShadow(this.scene, hit))
                {
                    // Tint the ambient light
                    Vector3D combined = VectorOperations.multiplyVectors(sphere.getMaterial().getVector(),
                            scene.getAmbientLight().getLightColor().getVector());
                    return new Color(combined);
                }
                return this.lighter.getFinalColor(new Color(sphere.getMaterial().getVector()),
                        sphereNormal.getVector(), this.scene.getDirectionalLight(), new Diffuse(phong.getVector(),
                                phong.getSpecularHighlight(), phong.getPhongConstant()), hit.getHitPoint().getVector(),
                        ray, this.scene, n, phong.getSpecularCoefficient(), i, j);
            }
            // Area lights should just return the basic light color
            else if (ClassIdentifier.isEmissive(sphere.getMaterial()))
            {
                return new Color(sphere.getMaterial().getVector());
            }
            else if (ClassIdentifier.isTexture(sphere.getMaterial()))
            {
                ImageTexture imageTexture = (ImageTexture) sphere.getMaterial();
                Color diffuseColor = this.texturer.getTextureColor(sphere, hit);
                if (this.shadower.isInShadow(this.scene, hit))
                {
                    // Tint the ambient light
                    Vector3D combined = VectorOperations.multiplyVectors(diffuseColor.getVector(),
                            scene.getAmbientLight().getLightColor().getVector());
                    return new Color(combined);
                }
                return this.lighter.getFinalColor(diffuseColor,
                        sphereNormal.getVector(), this.scene.getDirectionalLight(), imageTexture.getDiffuse(),
                        hit.getHitPoint().getVector(), ray, this.scene, n, Constants.DEFAULT_COEFFICIENT, i, j);
                //return diffuseColor;
            }
            return new Color(sphere.getMaterial().getVector());
        }
        else if (ClassIdentifier.isTriangle(shape))
        {
            Triangle triangle = (Triangle) shape;
            if (ClassIdentifier.isDiffuse(triangle.getMaterial()))
            {
                if (this.shadower.isInShadow(this.scene, hit))
                {
                    Vector3D combined = VectorOperations.multiplyVectors(triangle.getMaterial().getVector(),
                            scene.getAmbientLight().getLightColor().getVector());
                    return new Color(combined);
                }
                Vector3D ab = VectorOperations.subtractVectors(triangle.getVertex2().getVector(),
                        triangle.getVertex1().getVector());
                Vector3D ac = VectorOperations.subtractVectors(triangle.getVertex3().getVector(),
                        triangle.getVertex1().getVector());
                TriangleNormal triangleNormal = new TriangleNormal(ab, ac);
                return this.lighter.getFinalColor(new Color(triangle.getMaterial().getVector()),
                        triangleNormal.getVector(), this.scene.getDirectionalLight(), (Diffuse)triangle.getMaterial(),
                        hit.getHitPoint().getVector(), ray, this.scene, n, Constants.DEFAULT_COEFFICIENT, i, j);
            }
            return new Color(triangle.getMaterial().getVector());
        }
        return hitColor;
    }

    private Color getColorAtPosition(BackgroundImage image, int i, int j)
    {
        return image.getColorAtIndex(j, i);
    }

    public Pixel computeColorOfPixel(Ray ray, Color backgroundColor, int i, int j)
    {
        ArrayList<Hit> rayShapeHits = this.intersector.computeRayShapeHits(ray, this.scene.getShapes(), this.scene);
        if (this.raytracer.nothingHit(rayShapeHits))
        {
            if (this.scene.getBackgroundImage() != null)
            {
                return new Pixel(this.getColorAtPosition(scene.getBackgroundImage(), i, j));
            }
            return new Pixel(backgroundColor);
        }
        Hit closest = this.raytracer.getClosestHit(rayShapeHits);
        return new Pixel(this.colorShape(closest, ray, 0, i, j));
    }

    private PPMImage shootRayAtEachPixelAndLightIt(int dimension, PPMImage image)
    {
        System.out.println("Path tracing: Shooting rays...");
        for (int i = 0; i < dimension; i++)
        {
            for (int j = 0; j < dimension; j++)
            {
                // j and i must be switched due to how a PPM is structured
                Ray ray = this.raytracer.buildRay(j, i, dimension, this.scene.getCamera());
                Pixel coloredPixel = this.computeColorOfPixel(ray, this.scene.getBackgroundColor(), i, j);
                image.addPixel(coloredPixel, i, j);
            }
        }
        System.out.println("Performed " + Globals.numIntersectionTests + " intersection tests.");
        return image;
    }

    private PPMImage pathTrace(int dimension)
    {
        // Uncomment this if you want area/point lights ONLY
        this.scene.getDirectionalLight().turnOff();
        dimension = this.raytracer.antialiasDimension(dimension);
        PPMImage renderedImage = new PPMImage(dimension, dimension);
        renderedImage = shootRayAtEachPixelAndLightIt(dimension, renderedImage);
        return renderedImage;
    }

    public void render(int dimension, String fileName)
    {
        PPMImage image = this.pathTrace(dimension);
        assert image != null;
        ppmRenderer.writeImageToFile(image, this.getFileName(fileName));
    }
}
