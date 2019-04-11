package project;



import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.text.AttributedString;
import java.text.AttributedCharacterIterator;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.sound.midi.*;
import javax.sound.sampled.*;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.util.Vector;
import java.net.URL;


/**
 * A JukeBox for sampled and midi sound files.  Features duration progress, 
 * seek slider, pan and volume controls.
 *
 * @version @(#)Juke.java	1.19 00/01/31
 * @author Brian Lichtenwalter  
 */
public class Setting extends JPanel implements Runnable, LineListener, MetaEventListener, ControlContext {

    final int bufSize = 16384;
    PlaybackMonitor playbackMonitor = new PlaybackMonitor();

    Vector sounds = new Vector();  //파일 형태 지정
    Thread thread;
    Sequencer sequencer;
    boolean midiEOM, audioEOM;
    Synthesizer synthesizer;
    MidiChannel channels[];  //음원파일저장
    Object currentSound;
    String currentName;
    double duration;
    int num;
    boolean bump;
    boolean paused = false;
    JButton startB, pauseB, loopB, prevB, nextB;
    JTable table;
    JSlider panSlider, gainSlider;
    JSlider seekSlider;
    JukeTable jukeTable;
    Loading loading;
    Credits credits;
    String errStr;
    JukeControls controls;

    //setting -> loadJuke->addSound->sounds.add
    public Setting(String dirName) {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(5,5,5,5));

        if (dirName != null) {
            loadJuke(dirName); 
        }

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,jukeTable = new JukeTable(), controls = new JukeControls());
        splitPane.setContinuousLayout(true);
        add(splitPane);
    }//Setting


    public void open() {
        try {
            sequencer = MidiSystem.getSequencer();
			if (sequencer instanceof Synthesizer) {  //sequencer Synthesizer인스턴스면 true
				synthesizer = (Synthesizer)sequencer; //캐스팅
				channels = synthesizer.getChannels(); //bgm에 있는 음원파일들을 가져와서 배열에 넣음
			} 

        } catch (Exception ex) { ex.printStackTrace(); return; }
        sequencer.addMetaEventListener(this);
        (credits = new Credits()).start();
    }//open


    public void close() {
        if (credits != null && credits.isAlive()) {
            credits.interrupt();
        }
        if (thread != null && startB != null) {
            startB.doClick(0);
        }
        if (jukeTable != null && jukeTable.frame != null) {
            jukeTable.frame.dispose();
            jukeTable.frame = null;
        }
        if (sequencer != null) {
            sequencer.close();
        }
    }//close


    public void loadJuke(String name) {
        try {
            File file = new File(name);
            if (file != null && file.isDirectory()) {
                String files[] = file.list();  //노래 리스트 배열
                for (int i = 0; i < files.length; i++) {
                    File leafFile = new File(file.getAbsolutePath(), files[i]);  //프로그램을 실행시킨 위치도 함께 반환
                    if (leafFile.isDirectory()) {  //위치에 디렉터리가 존재하는지 확인
                        loadJuke(leafFile.getAbsolutePath());
                    } else {
                        addSound(leafFile); //아니면 Sound에 추가
                    }
                }
            } else if (file != null && file.exists()) {
                addSound(file);
            }
        } catch (SecurityException ex) {
            reportStatus(ex.toString());
        } catch (Exception ex) {
            reportStatus(ex.toString());
        }
    }//loadJuke


    private void addSound(File file) { 
        String s = file.getName();
        if ( s.endsWith(".mid")|| s.endsWith(".wav")){ //파일의 형식을 확인한다.
            sounds.add(file);
        }
    }//addSound


    public boolean loadSound(Object object) {  //object->노래제목
        duration = 0.0;
        (loading = new Loading()).start();
        if (object instanceof File) {  //File일때
	            currentName = ((File) object).getName();
	            playbackMonitor.repaint();
	            try {
	                currentSound = AudioSystem.getAudioInputStream((File) object);
	            } catch(Exception e1) {
	                // load midi & rmf as inputstreams for now
	                try { 
	                    currentSound = MidiSystem.getSequence((File) object);
	                } catch (Exception e2) { 
	                    try { 
	                        FileInputStream is = new FileInputStream((File) object);
	                        currentSound = new BufferedInputStream(is, 1024);
	                    } catch (Exception e3) { 
	                        e3.printStackTrace(); 
	                        currentSound = null;
	                        return false;
	                    }
	                }
	            }	
	
	        loading.interrupt();
	
	        // 사용자가 멈추거나 탭을 변경
	        if (sequencer == null) {
	            currentSound = null;
	            return false;
	        } 
	
	        if (currentSound instanceof AudioInputStream) {
	           try {
	                AudioInputStream stream = (AudioInputStream) currentSound; //지정된 오디오 형식,길이를 가지는 입력스트림
	                AudioFormat format = stream.getFormat(); //사운드스트림의 특정 데이터 배열을 지정
	                //오디오 형식에 저장된 정보를 검토->이진 사운드 데이터의 비트 해석               	
	                /**
	                 * we can't yet open the device for ALAW/ULAW playback,
	                 * convert ALAW/ULAW to PCM
	                 */
	                if ((format.getEncoding() == AudioFormat.Encoding.ULAW) ||
	                    (format.getEncoding() == AudioFormat.Encoding.ALAW)) {  //오디오 스트림에 사용되는 특정 데이터 표현 유형을 지정
	                    AudioFormat tmp = new AudioFormat(
	                                              AudioFormat.Encoding.PCM_SIGNED, 
	                                              format.getSampleRate(),
	                                              format.getSampleSizeInBits() * 2,
	                                              format.getChannels(),
	                                              format.getFrameSize() * 2,
	                                              format.getFrameRate(),
	                                              true);
	                    stream = AudioSystem.getAudioInputStream(tmp, stream);
	                    format = tmp;
	                }	                
	                //오디오출력에 사용
	                DataLine.Info info = new DataLine.Info(Clip.class, 
	                                          stream.getFormat(), 
	                                          ((int) stream.getFrameLength() *
	                                              format.getFrameSize()));	
	                Clip clip = (Clip) AudioSystem.getLine(info);
	                clip.addLineListener(this);
	                clip.open(stream);
	                currentSound = clip;
	            } catch (Exception ex) { 
					ex.printStackTrace(); 
					currentSound = null;
					return false;
	            }
	        } //if-AudioInputStream
	        gainSlider.setEnabled(true);      
	        duration = getDuration();	
        }//file일때
        return true;	
    } //loadSound


    public void playSound() {
        playbackMonitor.start();
        setGain();
        midiEOM = audioEOM = bump = false;
        if (currentSound instanceof Sequence || currentSound instanceof BufferedInputStream && thread != null) {
            sequencer.start();
            while (!midiEOM && thread != null && !bump) {
                try { thread.sleep(99); } catch (Exception e) {break;}
            }
            sequencer.stop();
            sequencer.close();
        } else if (currentSound instanceof Clip && thread != null) {
            Clip clip = (Clip) currentSound;
            clip.start();
            try { thread.sleep(99); } catch (Exception e) { }
            while ((paused || clip.isActive()) && thread != null && !bump) {
                try { thread.sleep(99); } catch (Exception e) {break;}
            }
            clip.stop();
            clip.close();
        }
        currentSound = null;
        playbackMonitor.stop();
    }//playSound

    public double getDuration() {
        double duration = 0.0;
        if (currentSound instanceof Sequence) {
            duration = ((Sequence) currentSound).getMicrosecondLength() / 1000000.0;
        }  else if (currentSound instanceof BufferedInputStream) {
			duration = sequencer.getMicrosecondLength() / 1000000.0;
		} else if (currentSound instanceof Clip) {
            Clip clip = (Clip) currentSound;
            duration = clip.getBufferSize() / 
                (clip.getFormat().getFrameSize() * clip.getFormat().getFrameRate());
        } 
        return duration;
    }//getDuration


    public double getSeconds() {
        double seconds = 0.0;
        if (currentSound instanceof Clip) {
            Clip clip = (Clip) currentSound;
            seconds = clip.getFramePosition() / clip.getFormat().getFrameRate();
        } else if ( (currentSound instanceof Sequence) || (currentSound instanceof BufferedInputStream) ) {
            try {
                seconds = sequencer.getMicrosecondPosition() / 1000000.0;
            } catch (IllegalStateException e){
                System.out.println("TEMP: IllegalStateException "+
                    "on sequencer.getMicrosecondPosition(): " + e);
            }
        }
        return seconds;
    } //getSeconds


    public void update(LineEvent event) {
        if (event.getType() == LineEvent.Type.STOP && !paused) { 
            audioEOM = true;
        }
    }//update


    public void meta(MetaMessage message) {
        if (message.getType() == 47) {  // 47 is end of track
            midiEOM = true;
        }
    }//meta


    private void reportStatus(String msg) {
        if ((errStr = msg) != null) {
            System.out.println(errStr);
            playbackMonitor.repaint();
        }
        if (credits != null && credits.isAlive()) {
            credits.interrupt();
        }
    }//reportStatus


    public Thread getThread() {
        return thread;
    }


    public void start() {
        thread = new Thread(this);
        thread.setName("Juke");
        thread.start();
    }


    public void stop() {
        if (thread != null) {
            thread.interrupt();
        }
        thread = null;
    }


    public void run() {
    //	playSound();
        do {
            table.scrollRectToVisible(new Rectangle(0,0,1,1));
            for (; num < sounds.size() && thread != null; num++) {
                table.scrollRectToVisible(new Rectangle(0,(num+2)*(table.getRowHeight()+table.getRowMargin()),1,1));
                table.setRowSelectionInterval(num, num);
                if( loadSound(sounds.get(num)) == true ) {
                    playSound();
		}
            }
            num = 0;
        } while (loopB.isSelected() && thread != null);

        if (thread != null) {
            startB.doClick();
        }
        thread = null;
        currentName = null;
        currentSound = null;
        playbackMonitor.repaint();
    }

    public void setGain() {  //소리 조절
        double value = gainSlider.getValue() / 100.0;
        if (currentSound instanceof Clip) {
            try {
                Clip clip = (Clip) currentSound;
                FloatControl gainControl = 
                  (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float dB = (float) 
                  (Math.log(value==0.0?0.0001:value)/Math.log(10.0)*20.0);
                gainControl.setValue(dB);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (currentSound instanceof Sequence || currentSound instanceof BufferedInputStream) {
            for (int i = 0; i < channels.length; i++) {                
				channels[i].controlChange(7, (int)(value * 127.0));

			}
        }else {}
    }//setGain

    /**
     * GUI controls for start, stop, previous, next, pan and gain.
     */
    class JukeControls extends JPanel implements ActionListener, ChangeListener {
    	//버튼 추가(addButton),볼륨조절(stateChanged),버튼 상태(setComponentsEnabled)
        public JukeControls() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            JPanel p1 = new JPanel();
           
            p1.setBorder(new EmptyBorder(10,0,5,0));
            JPanel p2 = new JPanel();
            startB = addButton("Start", p2, true);  //sounds.size() != 0
            pauseB = addButton("Pause", p2, true);
            p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));
            p1.add(p2);
            
            JPanel p3 = new JPanel();
            prevB = addButton("<<", p3, false);
            nextB = addButton(">>", p3, false);
            p1.add(p3);
            add(p1);    
            JPanel p4 = new JPanel(new BorderLayout());
            EmptyBorder eb = new EmptyBorder(5,20,10,20);
            BevelBorder bb = new BevelBorder(BevelBorder.LOWERED);
            p4.setBorder(new CompoundBorder(eb,bb));

            //볼륨조절하는 곳
            JPanel p5 = new JPanel();
            p5.setLayout(new BoxLayout(p5, BoxLayout.X_AXIS));
            p5.setBorder(new EmptyBorder(5,5,10,5));
            TitledBorder tb = new TitledBorder(new EtchedBorder());
            gainSlider = new JSlider(0, 100, 80);
            gainSlider.addChangeListener(this);
            tb = new TitledBorder(new EtchedBorder());
            tb.setTitle("볼륨 : 80");
            gainSlider.setBorder(tb);
            p5.add(gainSlider);
            add(p5);
        } //JukeControls 생성자

        private JButton addButton(String name, JPanel panel, boolean state) { //start,pause,<<,>>버튼 추가
            JButton b = new JButton(name);
            b.addActionListener(this);
            b.setEnabled(state);
            panel.add(b);
            return b;
        }

        public void stateChanged(ChangeEvent e) {  //볼륨 조절하는(class JukeControls에서 변화감지 시 실행)
            JSlider slider = (JSlider) e.getSource();
            int value = slider.getValue();
            TitledBorder tb = (TitledBorder) slider.getBorder();
            String s = tb.getTitle();
            if (s.startsWith("볼륨")) {
                s = s.substring(0, s.indexOf(':')+1) + s.valueOf(value);
                if (currentSound != null) {
                    setGain();
                }
            } 
            tb.setTitle(s);
            slider.repaint();
        }


        public void setComponentsEnabled(boolean state) { //넘겨받은 버튼 상태로 나머지설정
            pauseB.setEnabled(state);
            prevB.setEnabled(state);
            nextB.setEnabled(state);
        }

        public void actionPerformed(ActionEvent e) {  //버튼 동작
            JButton button = (JButton) e.getSource();
            if (button.getText().equals("Start")) {  //누른 버튼 Start면
                if (credits != null) {  credits.interrupt();  }
                paused = false;
                num = table.getSelectedRow();
                num = num == -1 ? 0 : num;
                start();
                button.setText("Stop");  
                setComponentsEnabled(true);  //버튼 상태넘겨줌
            } else if (button.getText().equals("Stop")) {
                credits = new Credits();
                credits.start();
                paused = false;
                stop();
                button.setText("Start");
                pauseB.setText("Pause");
                setComponentsEnabled(false);  //stop이니까 false
            } else if (button.getText().equals("Pause")) {
                paused = true;
                if (currentSound instanceof Clip) {
                    ((Clip) currentSound).stop();
                } else if (currentSound instanceof Sequence || currentSound instanceof BufferedInputStream) {
                    sequencer.stop();
                }
                playbackMonitor.stop();   //thread에 stop
                pauseB.setText("Resume");
            } else if (button.getText().equals("Resume")) {
                paused = false;
                if (currentSound instanceof Clip) {
                    ((Clip) currentSound).start();
                } else if (currentSound instanceof Sequence || currentSound instanceof BufferedInputStream) {
                    sequencer.start();
                }
                playbackMonitor.start();
                pauseB.setText("Pause");
            } else if (button.getText().equals("<<")) {
                paused = false;
                pauseB.setText("Pause");
                num = num-1 < 0 ? sounds.size()-1 : num-2;
                bump = true;
            } else if (button.getText().equals(">>")) {
                paused = false;
                pauseB.setText("Pause");
                num = num+1 == sounds.size() ? -1 : num;
                bump = true;
            }
        }
    }  // End JukeControls



    /**
     * Displays current sound and time elapsed.
     */
    public class PlaybackMonitor extends JPanel implements Runnable {
        Thread pbThread;
        Color black = new Color(20, 20, 20); 
        Color jfcBlue = new Color(204, 204, 255);
        Color jfcDarkBlue = jfcBlue.darker();
        Font font24 = new Font("serif", Font.BOLD, 24);
        Font font28 = new Font("serif", Font.BOLD, 28);
        Font font42 = new Font("serif", Font.BOLD, 42);
        FontMetrics fm28, fm42;
        
        public void start() {
            pbThread = new Thread(this);
            pbThread.setName("PlaybackMonitor");
            pbThread.start();
        }
        
        public void stop() {
            if (pbThread != null) {
                pbThread.interrupt();
            }
            pbThread = null;
        }
        
        public void run() {
            while (pbThread != null) {
                try {
                    pbThread.sleep(99);
                } catch (Exception e) { break; }
                repaint();
            }
            pbThread = null;
        }
    } // End PlaybackMonitor
            

    /**
     * Table to display the name of the sound.
     */
    class JukeTable extends JPanel implements ActionListener {

        TableModel dataModel;
        JFrame frame;
        JTextField textField;
        JButton applyB;

        public JukeTable() {
            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(260,300));
            final String[] names = { "#", "Name" };    
            dataModel = new AbstractTableModel() {
                public int getColumnCount() { return names.length; }
                public int getRowCount() { return sounds.size();}
                public Object getValueAt(int row, int col) { 
                    if (col == 0) {
                        return new Integer(row);
                    } else if (col == 1) {
                        Object object = sounds.get(row);
                        if (object instanceof File) {
                            return ((File) object).getName();
                        } else if (object instanceof URL) {
                            return ((URL) object).getFile();
                        }
                    } 
                    return null;
                }
                public String getColumnName(int col) {return names[col]; }
                public Class getColumnClass(int c) {
                    return getValueAt(0, c).getClass();
                }
                public boolean isCellEditable(int row, int col) {
                    return false;
                }
                public void setValueAt(Object aValue, int row, int col) {
                }
            };
    
            table = new JTable(dataModel);
            TableColumn col = table.getColumn("#");
            col.setMaxWidth(20);
            table.sizeColumnsToFit(0);
        
            JScrollPane scrollPane = new JScrollPane(table);
            EmptyBorder eb = new EmptyBorder(5,5,2,5);
            scrollPane.setBorder(new CompoundBorder(eb,new EtchedBorder()));
            add(scrollPane);
        }


        public void actionPerformed(ActionEvent e) {
            Object object = e.getSource();

            if (object instanceof JButton) {
                JButton button = (JButton) e.getSource();
            }
        }

        public void tableChanged() {
            table.tableChanged(new TableModelEvent(dataModel));
        }
    }  //JukeTable

    /**
     * Animation thread for when an audio file loads.
     */
    class Loading extends Thread {
        double extent; int incr;
        public void run() {
            extent = 360.0; incr = 10;
            while (true) {
                try { sleep(99); } catch (Exception ex) { break; }
                playbackMonitor.repaint();
            }
        }
    } //Loading           

    /**
     * Animation thread for the contributors of Java Sound.
     */
    class Credits extends Thread {
        int x;
        Font font16 = new Font("serif", Font.PLAIN, 16);
        String contributors = "Contributors : Kara Kytle, " + 
                              "Jan Borgersen, " + "Brian Lichtenwalter";
        int strWidth = getFontMetrics(font16).stringWidth(contributors);
        public void run() {
            x = -999; 
            while (!playbackMonitor.isShowing()) {
                try { sleep(999); } catch (Exception e) { return; }
            }
            for (int i = 0; i < 100; i++) {
                try { sleep(99); } catch (Exception e) { return; }
            }
            while (true) {
                if (--x < -strWidth) {
                    x = playbackMonitor.getSize().width;
                }
                playbackMonitor.repaint();
                try { sleep(99); } catch (Exception ex) { break; }
            }
        }
    }//Credits
            
} 
