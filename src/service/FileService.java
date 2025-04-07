package service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileService {
    public static <T> void saveToFile(List<T> data, String fileName){
        try(ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(fileName))){
                outStream.writeObject(data);
        }catch (IOException exception){
            System.out.println("Error saving to file "+fileName );
        }
    }

    public static <T> List<T> readFromFile(String fileName){
        File file = new File(fileName);

        if (!file.exists()) return new ArrayList<>();

        try (ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(fileName))){
            return (List<T>) inStream.readObject();
        }catch (IOException | ClassNotFoundException ex){
            System.out.println("Error reading from file "+fileName );
            return new ArrayList<>();
        }
    }
}
