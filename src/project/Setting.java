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

    Vector sounds = new Vector();  //���� ���� ����
    Thread thread;
    Sequencer sequencer;
    boolean midiEOM, audioEOM;
    Synthesizer synthesizer;
    MidiChannel channels[];  //������������
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
			if (sequencer instanceof Synthesizer) {  //sequencer Synthesizer�ν��Ͻ��� true
				synthesizer = (Synthesizer)sequencer; //ĳ����
				channels = synthesizer.getChannels(); //bgm�� �ִ� �������ϵ��� �����ͼ� �迭�� ����
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
                String files[] = file.list();  //�뷡 ����Ʈ �迭
                for (int i = 0; i < files.length; i++) {
                    File leafFile = new File(file.getAbsolutePath(), files[i]);  //���α׷��� �����Ų ��ġ�� �Բ� ��ȯ
                    if (leafFile.isDirectory()) {  //��ġ�� ���͸��� �����ϴ��� Ȯ��
                        loadJuke(leafFile.getAbsolutePath());
                    } else {
                        addSound(leafFile); //�ƴϸ� Sound�� �߰�
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
        if ( s.endsWith(".mid")|| s.endsWith(".wav")){ //������ ������ Ȯ���Ѵ�.
            sounds.add(file);
        }
    }//addSound


    public boolean loadSound(Object object) {  //object->�뷡����
        duration = 0.0;
        (loading = new Loading()).start();
        if (object instanceof File) {  //File�϶�
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
	
	        // ����ڰ� ���߰ų� ���� ����
	        if (sequencer == null) {
	            currentSound = null;
	            return false;
	        } 
	
	        if (currentSound instanceof AudioInputStream) {
	           try {
	                AudioInputStream stream = (AudioInputStream) currentSound; //������ ����� ����,���̸� ������ �Է½�Ʈ��
	                AudioFormat format = stream.getFormat(); //���彺Ʈ���� Ư�� ������ �迭�� ����
	                //����� ���Ŀ� ����� ������ ����->���� ���� �������� ��Ʈ �ؼ�               	
	                /**
	                 * we can't yet open the device for ALAW/ULAW playback,
	                 * convert ALAW/ULAW to PCM
	                 */
	                if ((format.getEncoding() == AudioFormat.Encoding.ULAW) ||
	                    (format.getEncoding() == AudioFormat.Encoding.ALAW)) {  //����� ��Ʈ���� ���Ǵ� Ư�� ������ ǥ�� ������ ����
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
	                //�������¿� ���
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
        }//file�϶�
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

    public void setGain() {  //�Ҹ� ����
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
    	//��ư �߰�(addButton),��������(stateChanged),��ư ����(setComponentsEnabled)
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

            //���������ϴ� ��
            JPanel p5 = new JPanel();
            p5.setLayout(new BoxLayout(p5, BoxLayout.X_AXIS));
            p5.setBorder(new EmptyBorder(5,5,10,5));
            TitledBorder tb = new TitledBorder(new EtchedBorder());
            gainSlider = new JSlider(0, 100, 80);
            gainSlider.addChangeListener(this);
            tb = new TitledBorder(new EtchedBorder());
            tb.setTitle("���� : 80");
            gainSlider.setBorder(tb);
            p5.add(gainSlider);
            add(p5);
        } //JukeControls ������

        private JButton addButton(String name, JPanel panel, boolean state) { //start,pause,<<,>>��ư �߰�
            JButton b = new JButton(name);
            b.addActionListener(this);
            b.setEnabled(state);
            panel.add(b);
            return b;
        }

        public void stateChanged(ChangeEvent e) {  //���� �����ϴ�(class JukeControls���� ��ȭ���� �� ����)
            JSlider slider = (JSlider) e.getSource();
            int value = slider.getValue();
            TitledBorder tb = (TitledBorder) slider.getBorder();
            String s = tb.getTitle();
            if (s.startsWith("����")) {
                s = s.substring(0, s.indexOf(':')+1) + s.valueOf(value);
                if (currentSound != null) {
                    setGain();
                }
            } 
            tb.setTitle(s);
            slider.repaint();
        }


        public void setComponentsEnabled(boolean state) { //�Ѱܹ��� ��ư ���·� ����������
            pauseB.setEnabled(state);
            prevB.setEnabled(state);
            nextB.setEnabled(state);
        }

        public void actionPerformed(ActionEvent e) {  //��ư ����
            JButton button = (JButton) e.getSource();
            if (button.getText().equals("Start")) {  //���� ��ư Start��
                if (credits != null) {  credits.interrupt();  }
                paused = false;
                num = table.getSelectedRow();
                num = num == -1 ? 0 : num;
                start();
                button.setText("Stop");  
                setComponentsEnabled(true);  //��ư ���³Ѱ���
            } else if (button.getText().equals("Stop")) {
                credits = new Credits();
                credits.start();
                paused = false;
                stop();
                button.setText("Start");
                pauseB.setText("Pause");
                setComponentsEnabled(false);  //stop�̴ϱ� false
            } else if (button.getText().equals("Pause")) {
                paused = true;
                if (currentSound instanceof Clip) {
                    ((Clip) currentSound).stop();
                } else if (currentSound instanceof Sequence || currentSound instanceof BufferedInputStream) {
                    sequencer.stop();
                }
                playbackMonitor.stop();   //thread�� stop
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
