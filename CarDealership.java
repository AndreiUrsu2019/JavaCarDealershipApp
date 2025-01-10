import java.io.*;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

// Main Class
public class CarDealershipApp {
    public static void main(String[] args) {
        Dealership dealership = new Dealership("City Motors");
        dealership.loadCarsFromJson("cars.json");

        // Add some cars
        dealership.addCar(new GasCar("Toyota", "Camry", 2022, 30000, 30));
        dealership.addCar(new ElectricCar("Tesla", "Model 3", 2021, 50000, 350));
        dealership.addCar(new HybridCar("Toyota", "Prius", 2023, 35000, 50, 600));

        // Display cars
        dealership.displayCars();

        // Modify a car
        dealership.modifyCar("Tesla", "Model 3", new ElectricCar("Tesla", "Model 3", 2022, 52000, 370));

        // Sell a car
        dealership.sellCar("Toyota", "Camry");

        // Display cars after selling
        dealership.displayCars();

        // Save cars to JSON
        dealership.saveCarsToJson("cars.json");
    }
}

// Car Class
abstract class Car {
    private String make;
    private String model;
    private int year;
    private double price;

    public Car(String make, String model, int year, double price) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.price = price;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public double getPrice() {
        return price;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return year + " " + make + " " + model + " - $" + price;
    }

    public abstract JSONObject toJson();

    public static Car fromJson(JSONObject json) {
        String type = json.getString("type");
        if (type.equals("GasCar")) {
            return new GasCar(
                json.getString("make"),
                json.getString("model"),
                json.getInt("year"),
                json.getDouble("price"),
                json.getInt("mpg")
            );
        } else if (type.equals("ElectricCar")) {
            return new ElectricCar(
                json.getString("make"),
                json.getString("model"),
                json.getInt("year"),
                json.getDouble("price"),
                json.getInt("range")
            );
        } else if (type.equals("HybridCar")) {
            return new HybridCar(
                json.getString("make"),
                json.getString("model"),
                json.getInt("year"),
                json.getDouble("price"),
                json.getInt("mpg"),
                json.getInt("range")
            );
        } else {
            throw new IllegalArgumentException("Unknown car type: " + type);
        }
    }
}

// GasCar Class
class GasCar extends Car {
    private int mpg;

    public GasCar(String make, String model, int year, double price, int mpg) {
        super(make, model, year, price);
        this.mpg = mpg;
    }

    public int getMpg() {
        return mpg;
    }

    public void setMpg(int mpg) {
        this.mpg = mpg;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("type", "GasCar");
        json.put("make", getMake());
        json.put("model", getModel());
        json.put("year", getYear());
        json.put("price", getPrice());
        json.put("mpg", mpg);
        return json;
    }

    @Override
    public String toString() {
        return super.toString() + " (GasCar: " + mpg + " MPG)";
    }
}

// ElectricCar Class
class ElectricCar extends Car {
    private int range;

    public ElectricCar(String make, String model, int year, double price, int range) {
        super(make, model, year, price);
        this.range = range;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("type", "ElectricCar");
        json.put("make", getMake());
        json.put("model", getModel());
        json.put("year", getYear());
        json.put("price", getPrice());
        json.put("range", range);
        return json;
    }

    @Override
    public String toString() {
        return super.toString() + " (ElectricCar: " + range + " miles range)";
    }
}

// HybridCar Class
class HybridCar extends GasCar {
    private int range;

    public HybridCar(String make, String model, int year, double price, int mpg, int range) {
        super(make, model, year, price, mpg);
        this.range = range;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("type", "HybridCar");
        json.put("make", getMake());
        json.put("model", getModel());
        json.put("year", getYear());
        json.put("price", getPrice());
        json.put("mpg", getMpg());
        json.put("range", range);
        return json;
    }

    @Override
    public String toString() {
        return super.toString() + " (HybridCar: " + getMpg() + " MPG, " + range + " miles range)";
    }
}

// Dealership Class
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;

class Dealership {
    private String name;
    private ArrayList<Car> cars;

    public Dealership(String name) {
        this.name = name;
        this.cars = new ArrayList<>();
    }

    public void addCar(Car car) {
        cars.add(car);
        System.out.println("Added: " + car);
    }

    public void modifyCar(String make, String model, Car newCar) {
        for (int i = 0; i < cars.size(); i++) {
            Car car = cars.get(i);
            if (car.getMake().equalsIgnoreCase(make) && car.getModel().equalsIgnoreCase(model)) {
                cars.set(i, newCar);
                System.out.println("Modified: " + newCar);
                return;
            }
        }
        System.out.println("Car not found: " + make + " " + model);
    }

    public void sellCar(String make, String model) {
        for (Car car : cars) {
            if (car.getMake().equalsIgnoreCase(make) && car.getModel().equalsIgnoreCase(model)) {
                cars.remove(car);
                System.out.println("Sold: " + car);
                return;
            }
        }
        System.out.println("Car not found: " + make + " " + model);
    }

    public void displayCars() {
        System.out.println("\nCars available at " + name + ":");
        for (Car car : cars) {
            System.out.println(car);
        }
    }

    public void loadCarsFromJson(String filename) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filename)));
            JSONArray jsonArray = new JSONArray(content);
            cars.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                cars.add(Car.fromJson(jsonArray.getJSONObject(i)));
            }
            System.out.println("Loaded cars from " + filename);
        } catch (IOException e) {
            System.out.println("Error reading file " + filename + ": " + e.getMessage());
        }
    }

    public void saveCarsToJson(String filename) {
        JSONArray jsonArray = new JSONArray();
        for (Car car : cars) {
            jsonArray.put(car.toJson());
        }
        try (FileWriter file = new FileWriter(filename)) {
            file.write(jsonArray.toString());
            System.out.println("Saved cars to " + filename);
        } catch (IOException e) {
            System.out.println("Error writing to file " + filename + ": " + e.getMessage());
        }
    }
}
