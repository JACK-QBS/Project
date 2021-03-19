package demo;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GenerateDemoSession {
    private static class User implements Serializable {
        private String username;
        private String password;

        User(String username,String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        public String toString() {
            return "User{" +
                    "username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }

    public static void main(String[] args) throws IOException {
        String sessionId = UUID.randomUUID().toString();
        System.out.println(sessionId);
        Map<String,Object> map = new HashMap<>();
        map.put("currentUser",new User("qbs","123"));
        map.put("teacher",new User("zzw","456"));

        String filename = String.format("%s\\%s.session","D:\\Code\\Project\\Server\\sessions",sessionId);
        try (OutputStream os = new FileOutputStream(filename)) {
            try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(os)) {
                objectOutputStream.writeObject(map);
                objectOutputStream.flush();
            }
        }
    }
}
