import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.GzipCodec;
import org.junit.Test;

import java.io.IOException;

/**
 * The world always makes way for the dreamer
 * Created by 努力常态化 on 2018/8/15 09:23.
 * <p>
 * SequenceFile的crud操作测试
 */
public class TestSeqFile {

    @Test
    public void save() throws IOException {
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", "file:///");
        FileSystem fileSystem = FileSystem.get(configuration);
        Path path = new Path("D:\\workDir\\pic\\1.seq");

        SequenceFile.Writer writer = SequenceFile.createWriter(fileSystem, configuration, path, IntWritable.class, Text.class);

        for (int i = 0; i < 10; i++) {
            writer.append(new IntWritable(i), new Text(" tom " + i));
            writer.sync();
        }

        for (int i = 0; i < 10; i++) {
            writer.append(new IntWritable(i), new Text(" jim " + i));

            if (i % 2 == 0) {
                writer.sync();
            }
        }

        writer.close();
    }

    @Test
    public void save2() throws IOException {
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", "file:///");
        FileSystem fileSystem = FileSystem.get(configuration);
        Path path = new Path("D:\\workDir\\pic\\1.seq");

        SequenceFile.Writer writer = SequenceFile.createWriter(fileSystem, configuration,
                path, IntWritable.class, Text.class, SequenceFile.CompressionType.BLOCK, new GzipCodec());

        for (int i = 0; i < 10; i++) {
            writer.append(new IntWritable(i), new Text(" tom " + i));
            writer.sync();
        }

        for (int i = 0; i < 10; i++) {
            writer.append(new IntWritable(i), new Text(" jim " + i));

            if (i % 2 == 0) {
                writer.sync();
            }
        }

        writer.close();
    }

    @Test
    public void read() throws IOException {
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", "file:///");
        FileSystem fileSystem = FileSystem.get(configuration);
        Path path = new Path("D:\\workDir\\pic\\1.seq");

        SequenceFile.Reader reader = new SequenceFile.Reader(fileSystem, path, configuration);
        IntWritable key = new IntWritable();
        Text value = new Text();

        while (reader.next(key, value)) {
            System.out.println(key.get() + " ----- key and value -----" + value.toString());
        }

        reader.close();
    }

    /**
     * 读操作,得到当前value
     */
    @Test
    public void read2() throws IOException {
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", "file:///");
        FileSystem fileSystem = FileSystem.get(configuration);
        Path path = new Path("D:\\workDir\\pic\\1.seq");

        SequenceFile.Reader reader = new SequenceFile.Reader(fileSystem, path, configuration);
        IntWritable key = new IntWritable();
        Text value = new Text();

        while (reader.next(key)) {
            reader.getCurrentValue(value);
            System.out.println(value.toString());
        }

        reader.close();
    }

    /**
     * 读操作，指定位置开始读
     * <p>
     * seek 必须是边界
     */
    @Test
    public void read3() throws IOException {
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", "file:///");
        FileSystem fileSystem = FileSystem.get(configuration);
        Path path = new Path("D:\\workDir\\pic\\1.seq");

        SequenceFile.Reader reader = new SequenceFile.Reader(fileSystem, path, configuration);
        IntWritable key = new IntWritable();
        Text value = new Text();

        reader.seek(202);

        reader.next(key);
        reader.getCurrentValue(value);
        System.out.println(key.get() + " ----- key and value -----" + value.toString());
        reader.close();
    }

    /**
     * 从指定位置开始读取下一个同步点
     */
    @Test
    public void read4() throws IOException {
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", "file:///");
        FileSystem fileSystem = FileSystem.get(configuration);
        Path path = new Path("D:\\workDir\\pic\\1.seq");

        SequenceFile.Reader reader = new SequenceFile.Reader(fileSystem, path, configuration);
        IntWritable key = new IntWritable();
        Text value = new Text();

        reader.sync(20);

        while (reader.next(key, value)) {
            System.out.println(reader.getPosition() + " ---" + key.get() + " ----- key and value -----" + value.toString());
        }

        reader.close();
    }

}
