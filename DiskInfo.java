package dick;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class DiskInfo {

    public static void main(String[] args) {

        System.out.println("=== ВСТАНОВЛЕНІ ФІЗИЧНІ ДИСКИ ===");

        getDiskModels();

        System.out.println("\n=== ТЕСТ ШВИДКОСТІ ВСІХ ЛОГІЧНИХ ДИСКІВ ===");

        File[] roots = File.listRoots();

        for (File root : roots) {
            System.out.println("\nПеревіряється диск: " + root.getAbsolutePath());
            testDiskSpeed(root);
        }
    }

    // Отримання моделей фізичних дисків
    public static void getDiskModels() {
        try {
            Process process = Runtime.getRuntime().exec(
                    "wmic diskdrive get Model,InterfaceType,Size"
            );

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Тест швидкості для конкретного диска
    public static void testDiskSpeed(File disk) {
        try {
            Path path = Paths.get(disk.getAbsolutePath() + "speed_test.tmp");

            byte[] data = new byte[1024 * 1024]; // 1 MB блок
            FileOutputStream fos = new FileOutputStream(path.toFile());

            // Тест запису 50 MB
            long writeStart = System.nanoTime();
            for (int i = 0; i < 50; i++) {
                fos.write(data);
            }
            fos.close();
            long writeEnd = System.nanoTime();

            double writeSeconds = (writeEnd - writeStart) / 1_000_000_000.0;
            double writeSpeed = 50 / writeSeconds;

            // Тест читання
            long readStart = System.nanoTime();
            FileInputStream fis = new FileInputStream(path.toFile());
            while (fis.read(data) != -1) {}
            fis.close();
            long readEnd = System.nanoTime();

            double readSeconds = (readEnd - readStart) / 1_000_000_000.0;
            double readSpeed = 50 / readSeconds;

            System.out.printf("Назва диска: %s\n", disk.getAbsolutePath());
            System.out.printf("Швидкість запису: %.2f MB/s\n", writeSpeed);
            System.out.printf("Швидкість читання: %.2f MB/s\n", readSpeed);

            Files.delete(path);

        } catch (Exception e) {
            System.out.println("Не вдалося протестувати диск " + disk.getAbsolutePath());
        }
    }
}