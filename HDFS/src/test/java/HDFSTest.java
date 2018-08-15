import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.junit.Test;

import java.io.IOException;

/**
 * The world always makes way for the dreamer
 * Created by 努力常态化 on 2018/8/6 09:27.
 */

public class HDFSTest {
    @Test
    public void run() throws IOException {
        Configuration configuration = new Configuration();
//        configuration.set("fs.defaultFS", "hdfs://192.168.68.201:8020/");
        FileSystem fileSystem = FileSystem.get(configuration);
//        FSDataInputStream fsDataInputStream = fileSystem.open(new Path("hdfs://192.168.68.201:8020/user/lanp/hi.txt"));
        Path path = new Path("hdfs:///");
        FSDataInputStream fsDataInputStream = fileSystem.open(path);
        IOUtils.copyBytes(fsDataInputStream, System.out, 1024, false);
    }

}
