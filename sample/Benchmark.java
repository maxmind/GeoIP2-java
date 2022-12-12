import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import com.maxmind.db.CHMCache;
import com.maxmind.db.NoCache;
import com.maxmind.db.NodeCache;
import com.maxmind.db.Reader.FileMode;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

public class Benchmark {

    private final static int COUNT = 1000000;
    private final static int WARMUPS = 3;
    private final static int BENCHMARKS = 5;
    private final static boolean TRACE = false;

    public static void main(String[] args) throws GeoIp2Exception, IOException {
        File file = new File(args.length > 0 ? args[0] : "GeoLite2-City.mmdb");
        System.out.println("No caching");
        loop("Warming up", file, WARMUPS, NoCache.getInstance());
        loop("Benchmarking", file, BENCHMARKS, NoCache.getInstance());

        System.out.println("With caching");
        loop("Warming up", file, WARMUPS, new CHMCache());
        loop("Benchmarking", file, BENCHMARKS, new CHMCache());
    }

    private static void loop(String msg, File file, int loops, NodeCache cache)
        throws GeoIp2Exception, IOException {
        System.out.println(msg);
        for (int i = 0; i < loops; i++) {
            DatabaseReader r =
                new DatabaseReader.Builder(file).fileMode(FileMode.MEMORY_MAPPED).withCache(cache)
                    .build();
            bench(r, COUNT, i);
        }
        System.out.println();
    }

    private static void bench(DatabaseReader r, int count, int seed)
        throws GeoIp2Exception, UnknownHostException {
        Random random = new Random(seed);
        long startTime = System.nanoTime();
        byte[] address = new byte[4];
        for (int i = 0; i < count; i++) {
            random.nextBytes(address);
            InetAddress ip = InetAddress.getByAddress(address);
            CityResponse t;
            try {
                t = r.city(ip);
            } catch (AddressNotFoundException | IOException e) {
            }
            if (TRACE) {
                if (i % 50000 == 0) {
                    System.out.println(i + " " + ip);
                    System.out.println(t);
                }
            }
        }
        long endTime = System.nanoTime();

        long duration = endTime - startTime;
        long qps = count * 1000000000L / duration;
        System.out.println("Requests per second: " + qps);
    }
}
