package com.example.androidtdd;

import com.example.androidtdd.data.model.Address;
import com.example.androidtdd.data.model.Comment;
import com.example.androidtdd.data.model.Company;
import com.example.androidtdd.data.model.Photo;
import com.example.androidtdd.data.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by quanlt on 25/01/2017.
 */

public class MockHelper {
    private static Address makeAddress() {
        Address address = new Address();
        address.setCity("Gwenborough");
        address.setSuite("Apt. 556");
        address.setStreet("Kulas Light");
        return address;
    }

    private static Company makeCompany() {
        Company company = new Company();
        company.setBs("Romaguera-Crona");
        company.setCatchPhrase("Multi-layered client-server neural-net");
        company.setName("Romaguera-Crona");
        return company;
    }

    public static User makeUser(int userId) {
        User user = new User();
        user.setId(userId);
        user.setName("Leanne Graham");
        user.setEmail("Sincere@april.biz");
        user.setAddress(makeAddress());
        user.setCompany(makeCompany());
        user.setPhone("1-770-736-8031");
        user.setWebsite("hildegard.org");
        return user;
    }

    public static String readUsersString() throws IOException {
        InputStream stream = MockHelper.class.getClassLoader().getResourceAsStream("users.json");
        Scanner s = new Scanner(stream).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";
        stream.close();
        return result;
    }

    public static String readPhotosString() throws IOException {
        InputStream stream = MockHelper.class.getClassLoader().getResourceAsStream("photos.json");
        Scanner s = new Scanner(stream).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";
        stream.close();
        return result;
    }

    public static String readCommentsString() throws IOException {
        InputStream stream = MockHelper.class.getClassLoader().getResourceAsStream("comments.json");
        Scanner s = new Scanner(stream).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";
        stream.close();
        return result;
    }

    public static List<User> getUsers(String json, Gson gson) {
        Type userType = new TypeToken<ArrayList<User>>() {
        }.getType();
        return gson.fromJson(json, userType);
    }

    public static List<Photo> getPhotos(String json, Gson gson) {
        Type photoType = new TypeToken<ArrayList<Photo>>() {
        }.getType();
        return gson.fromJson(json, photoType);
    }

    public static List<Comment> getComments(String json, Gson gson) {
        Type commentType = new TypeToken<ArrayList<Comment>>() {
        }.getType();
        return gson.fromJson(json, commentType);
    }

    public static List<Photo> getPhotos() throws IOException {
        return getPhotos(readPhotosString(), new Gson());
    }

    public static List<Comment> getComments() throws IOException {
        return getComments(readCommentsString(), new Gson());
    }

    public static List<User> getUsers() throws IOException {
        return getUsers(readUsersString(), new Gson());
    }
}
