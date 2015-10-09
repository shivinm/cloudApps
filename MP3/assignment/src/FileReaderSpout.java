
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.io.File;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

public class FileReaderSpout implements IRichSpout {
    private SpoutOutputCollector _collector;
    private TopologyContext context;
    BufferedReader _br;  

  @Override
  public void open(Map conf, TopologyContext context,
                   SpoutOutputCollector collector) {

      try { 
          File f = new File("/root/cloudapp-mp3/data.txt");
          _br = new BufferedReader(new FileReader(f));
      } 
      catch (IOException e)
      {
          System.out.println("File I/O error!");
      }

      this.context = context;
      this._collector = collector;
  }

  @Override
  public void nextTuple() {

      try { 
          String line = _br.readLine();
          while (line != null) {
              _collector.emit(new Values(line));
              line = _br.readLine();
          }
      }
      catch (IOException e) {
          System.out.println("File I/O error!");
      }

      Utils.sleep(100);
     /*
    ----------------------TODO-----------------------
    Task:
    1. read the next line and emit a tuple for it
    2. don't forget to sleep when the file is entirely read to prevent a busy-loop

    ------------------------------------------------- */


  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer) {

    declarer.declare(new Fields("word"));

  }

  @Override
  public void close() {
      try { 
          _br.close();
      }
      catch (IOException e) {
          System.out.println("File I/O error!");
      }
  }


  @Override
  public void activate() {
  }

  @Override
  public void deactivate() {
  }

  @Override
  public void ack(Object msgId) {
  }

  @Override
  public void fail(Object msgId) {
  }

  @Override
  public Map<String, Object> getComponentConfiguration() {
    return null;
  }
}
