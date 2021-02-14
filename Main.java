package com.company;

import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.lang.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import static java.lang.Math.round;

interface global
{
    //public static int[] totalhours={7,6,8,9,6,6,6,4,3};
    int[] totalhoursAug={7,6,8,9,6,6,6,4,3};
    int[] totalhoursOct = {13, 8, 8, 0, 10, 0, 10, 8, 0};
    int[] totalhoursSep = {12, 10, 8, 14, 6, 8 , 8 ,10, 3};
    public static int[] sindex={2,6,10,14,18,22,26,30,34};
    public static String[] courseName={"Linear Algebra","Data Structures","Computer Architecture","Discrete Structures","OOP","Economics for Engineers","DSLab","OOPLab","EVS"};
    int numCourses=(courseName.length);
}

class RollNum{
    static String[] students = {"19z301", "19z302", "19z303", "19z304","19z305","19z306","19z307","19z308","19z309","19z310","19z311","19z312","19z313","19z314","19z315","19z316","19z317","19z318","19z320","19z321","19z322","19z323","19z324", "19z326", "19z327", "19z328", "19z329", "19z330", "19z331", "19z332", "19z333", "19z334", "19z335", "19z336", "19z337", "19z338", "19z339", "19z340", "19z341", "19z342", "19z343", "19z344", "19z345", "19z346", "19z347", "19z348", "19z349", "19z350", "19z351", "19z352", "19z353", "19z354", "19z355", "19z356", "19z357", "19z358", "19z359", "19z360", "19z361", "19z362", "19z363", "19z364", "19izus011", "19izus009", "19izus010", "19izus012", "19izus013", "19izus014", "19izus015", "19izus016", "19izus017"};

    static String check_roll(String check){
        for (String s : students){
            if (s.equals(check)){
                return s;
            }
        }
        return null;
    }

    static int indexRoll(String rollNumber){
        for (int i = 0; i < students.length; i++){
            if (students[i].equals(rollNumber)){
                return i;
            }
        }
        return -1;
    }

}

class Student
{
    public Student(String roll,String nam,int[] hrs)
    {
        rollno=roll;
        name=nam;
        hours=hrs;
    }

    private String name;
    private String rollno;
    private int[] hours=new int[9];


    String getname()
    {
        return name;
    }
    String getrollno()
    {
        return rollno;
    }
    int[] gethours()
    {
        return hours;
    }
}

class Percentage extends Student implements global
{
    double[] per=new double[numCourses];
    double[] finalper=new double[numCourses];
    public double[] getper()
    {
        return per;
    }
    public double[] getfinalper()
    {
        return finalper;
    }

    public Percentage(String roll, String nam, int[] hrs) {
        super(roll, nam, hrs);
    }

    public void calcper(int[] hrs,int[] totalhours)
    {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        Arrays.fill(per,0.0);
        for(int i=0;i<numCourses;i++)
        {
            per[i]= Double.parseDouble(df.format((hrs[i]*100.0/totalhours[i])));
        }
    }

    public void compensation(int[] hrs,int late,int excused,int[] totalhours)
    {

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        for(int i=0;i<numCourses;i++)
        {   double fhours=hrs[i];
            if(late<4 && fhours<=totalhours[i])
            {
                fhours+=(1-(0.25*late));

            }
            if(excused<6 && fhours<=totalhours[i])
            {
                fhours += excused;
            }
            if(fhours>totalhours[i])
            {
                fhours=totalhours[i];
            }
            finalper[i]=Double.parseDouble(df.format(fhours*100.00/totalhours[i]));

        }
    }

}

// displays warning if attendance is less than 75% including compensation percentage.
class StudentWarning implements global
{
    ArrayList<Percentage> studwarning=new ArrayList<Percentage>();

    public void Warning(ArrayList<Percentage> student)
    {   System.out.println("The following students have attendance below 75%\n\n");
        for(int i=0;i<student.size();i++)
        {
            Percentage s = student.get(i);

            for(int j=0;j<(numCourses);j++)
            {
                if(s.per[j]<75 && s.per[j]!=0)
                {
                    System.out.printf("%s ",s.getrollno());
                    System.out.printf("%s ",s.getname());
                    System.out.printf("\t%s :",courseName[j]);
                    System.out.print(s.per[j]+"%");
                    studwarning.add(s);
                    System.out.println("\n");
                    break;
                }
            }
        }

    }
}

class Csvfile implements global
{
    public static void readFile(String month, int subIndex, String flag) {
        System.out.println();
        System.out.println(month);
        System.out.println();
        String csvsplit = ",";
        ArrayList<Percentage> studlist=new ArrayList<Percentage>();
        try {
            BufferedReader csv = new BufferedReader(new FileReader("C:\\Users\\Chandhini Bala\\Downloads\\Attendance - " + month + ".csv"));
            String line = csv.readLine();
            line = csv.readLine();
            line = csv.readLine();
            line = csv.readLine();
            while ((line = csv.readLine()) != null) {
                String[] sub = line.split(csvsplit);
                String rollno = sub[0];
                String name = sub[1];
                int[] hours = new int[numCourses];
                int[] late=new int[numCourses];
                int[] excuse=new int[numCourses];
                for (int i = 0; i < numCourses; i++) {
                    hours[i] = Integer.parseInt(sub[sindex[i]]);
                    late[i]=Integer.parseInt(sub[sindex[i]+1]);
                    excuse[i]=Integer.parseInt(sub[sindex[i]+2]);
                }
                Percentage s = new Percentage(sub[0], sub[1], hours);
                if(month.equals("October"))
                {
                    s.calcper(s.gethours(),totalhoursOct);
                }
                else if(month.equals("September"))
                {
                    s.calcper(s.gethours(),totalhoursSep);
                }
                else {
                    s.calcper(s.gethours(), totalhoursAug);
                }
                for(int i=0;i<(numCourses);i++)
                {
                    if((late[i]>0 || excuse[i]>0) && month.equals("October"))
                    {
                        s.compensation(s.gethours(),late[i],excuse[i],totalhoursOct);
                    }
                    else if((late[i]>0 || excuse[i]>0) && month.equals("September"))
                    {
                        s.compensation(s.gethours(),late[i],excuse[i],totalhoursSep);
                    }
                    else if((late[i]>0 || excuse[i]>0) && month.equals("August"))
                    {
                        s.compensation(s.gethours(),late[i],excuse[i],totalhoursSep);
                    }
                    else
                    {
                        s.finalper[i]=s.per[i];
                    }
                }
                studlist.add(s);

            }

            if (flag.equals("0")){
                JTableExamples j=new JTableExamples();
                j.create(studlist, subIndex);//teach
                csv.close();
            }
            else{
                JTableExamples j=new JTableExamples();
                int studentNo = RollNum.indexRoll(flag);
                Percentage obj=studlist.get(studentNo);
                j.teach(obj);//student
                csv.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally
        {
            StudentWarning w= new StudentWarning();
            System.out.println();
            w.Warning(studlist);
        }
    }
}
class JTableExamples implements global{
    JFrame f;
    JTable j;

    public void create(ArrayList<Percentage> p, int subInd) {

        // Frame initialization
        String[][] str = new String[p.size()][3];
        f = new JFrame();

        // Frame Title
        f.setTitle("ATTENDANCE REPORT");

        for (int j = 0; j < p.size(); j++) {
            Percentage ob = p.get(j);
            double[] per = ob.getper();
            str[j][0] = ob.getrollno();
            str[j][1] = ob.getname();
            str[j][2] = String.valueOf(per[subInd]);
        }
        // Column Names
        String[] columnNames = {"NAME", "ROLL NO","PERCENTAGE"};

        // Initializing the JTable
        j = new JTable(str,columnNames);

        j.setBounds(30, 40, 500, 700);

        // adding it to JScrollPane
        JScrollPane sp = new JScrollPane(j);
        f.add(sp);
        // Frame Size
        f.setSize(500, 200);
        // Frame Visible = true
        f.setVisible(true);
    }

    public void teach(Percentage obj){

        // Frame initiallization
        double [] d = new double[numCourses];
        d=obj.getper();
        String[][] r = new String[2][numCourses];
        String[] temp=new String[numCourses];
        String[] b={" "};

        for(int i=0;i<numCourses;i++) {
            temp[i] = String.valueOf((d[i]));
        }
        r[0]=temp;
        d=obj.getfinalper();
        for(int i=0;i<numCourses;i++) {
            temp[i] = String.valueOf((d[i]));
        }
        r[1]=temp;
        f = new JFrame();

        // Frame Title
        f.setTitle("STUDENT REPORT");

        CSVFile Rd = new CSVFile();

        String[] columnNames = courseName;
        j = new JTable(r,columnNames);

        j.setBounds(30, 40, 500, 700);

        // adding it to JScrollPane
        JScrollPane sp = new JScrollPane(j);
        f.add(sp);
        // Frame Size
        f.setSize(500, 200);
        // Frame Visible = true
        f.setVisible(true);
    }

    public class CSVFile {
        private final ArrayList<String[]> Rs = new ArrayList<String[]>();
        private String[] OneRow;

        public ArrayList<String[]> ReadCSVfile(File DataFile) {
            try {
                BufferedReader brd = new BufferedReader(new FileReader(DataFile));
                while (brd.ready()) {
                    String st = brd.readLine();
                    OneRow = st.split(",");
                    Rs.add(OneRow);
                    System.out.println(Arrays.toString(OneRow));
                } // end of while
            } // end of try
            catch (Exception e) {
                String errmsg = e.getMessage();
                System.out.println("File not found:" + errmsg);
            } // end of Catch
            return Rs;
        }// end of ReadFile method
    }// end of CSVFile class
}

class StudentInterface2 implements global
{
    public static void studentReport(String roll){
        JPanel panel4 = new JPanel();
        JFrame frame4 = new JFrame();
        frame4.setSize(500, 500);
        frame4.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame4.add(panel4);

        panel4.setLayout(null);

        JLabel monthLabel = new JLabel("Enter month of Attendance to view");
        monthLabel.setBounds(50, 20, 200, 25);
        panel4.add(monthLabel);

        JTextField monthText = new JTextField(10);
        monthText.setBounds(300, 20,160,25);
        panel4.add(monthText);

        JButton nextButton = new JButton("Next");
        nextButton.setBounds(150, 80,80,25);
        panel4.add(nextButton);
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String month = monthText.getText();
                Csvfile file= new Csvfile();
                file.readFile(month, 0, roll);
            }
        });
        frame4.setVisible(true);
    }
}

class StudentInterface extends JFrame{

    StudentInterface(String roll_num){
        JPanel panel = new JPanel();
        JFrame frame = new JFrame();
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(panel);

        panel.setLayout(null);

        JLabel welcome = new JLabel("Welcome " + roll_num + "!");
        welcome.setBounds(50, 20, 165, 25);
        panel.add(welcome);

        JButton button1 = new JButton("View your Attendance Report");
        button1.setBounds(50, 50,300,25);
        panel.add(button1);
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Stack.studentReport(roll_num);
            }
        });
        frame.setVisible(true);
    }
}


class TeacherInterface2 implements global {
    public static void saveRecord(String month)
    {
        JPanel panel = new JPanel();
        JFrame frame = new JFrame();
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(panel);

        panel.setLayout(null);

        JLabel title = new JLabel("Add Student");
        title.setBounds(100, 20, 165, 25);
        panel.add(title);

        JLabel rollLabel = new JLabel("Roll no.");
        rollLabel.setBounds(50, 50, 80, 25);
        panel.add(rollLabel);

        JTextField rollText = new JTextField(10);
        rollText.setBounds(150, 50, 165, 25);
        panel.add(rollText);

        JLabel nameLabel = new JLabel("Name");
        nameLabel.setBounds(50, 80, 80, 25);
        panel.add(nameLabel);

        JTextField nameText = new JTextField(20);
        nameText.setBounds(150, 80, 165, 25);
        panel.add(nameText);

        JButton saveButton = new JButton("Save");
        saveButton.setBounds(50, 110,80,25);
        panel.add(saveButton);
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String ID = rollText.getText();
                    String name = nameText.getText();
                    String filepath = "C:\\Users\\Chandhini Bala\\Downloads\\Attendance - " + month + ".csv";

                    FileWriter fw = new FileWriter(filepath, true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter pw = new PrintWriter(bw);

                    pw.println(ID + "," + name);
                    pw.flush();
                    pw.close();

                    JOptionPane.showMessageDialog(null, "Record saved");
                } catch (Exception e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Record NOT saved");
                }
            }
        });
        frame.setVisible(true);
    }

    public static void deleteRecord(String month)
    {
        JPanel panel = new JPanel();
        JFrame frame = new JFrame();
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(panel);

        panel.setLayout(null);

        JLabel title = new JLabel("Delete Record");
        title.setBounds(100, 20, 165, 25);
        panel.add(title);

        JLabel removeTerm = new JLabel("Enter roll no to delete");
        removeTerm.setBounds(50, 50, 165, 25);
        panel.add(removeTerm);

        JTextField rollText = new JTextField(10);
        rollText.setBounds(200, 50, 165, 25);
        panel.add(rollText);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBounds(50, 110,80,25);
        panel.add(deleteButton);
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String removeterm = rollText.getText();

                String filepath = "C:\\Users\\Chandhini Bala\\Downloads\\Attendance - " + month +".csv";
                String tempFile = "text.txt";
                String csvsplit = ",";

                //To create and delete a file later easily we make a File
                File newFile = new File(tempFile);
                File oldFile = new File(filepath);

                try {
                    BufferedWriter bw= new BufferedWriter(new FileWriter(tempFile,true));
                    PrintWriter pw = new PrintWriter(bw);

                    FileReader fr= new FileReader(filepath);
                    BufferedReader csv= new BufferedReader(fr);

                    String currentLine= csv.readLine();
                    currentLine = csv.readLine();
                    currentLine = csv.readLine();

                    while((currentLine=csv.readLine())!=null)
                    {
                        String[] data=currentLine.split(csvsplit);
                        if(!(data[0].equalsIgnoreCase(removeterm)))
                        {
                            pw.println(currentLine);
                        }
                    }
                    pw.flush();
                    pw.close();
                    fr.close();
                    csv.close();
                    bw.close();

                    oldFile.delete();
                    File dump= new File(filepath);
                    newFile.renameTo(dump);
                    JOptionPane.showMessageDialog(null, "Record deleted");
                }

                catch (Exception e2)
                {
                    JOptionPane.showMessageDialog(null,"Record NOT deleted");
                    e2.printStackTrace();
                }
            }
        });
        frame.setVisible(true);
    }

    public static void modifyRecord(int newindex, String term, String newterm, String month){
        String tempFile = "text.txt";
        String csvsplit = ",";
        String filepath = "C:\\Users\\Chandhini Bala\\Downloads\\Attendance - " + month + ".csv";

        //To create and delete a file later easily we make a File
        File newFile = new File(tempFile);
        File oldFile = new File(filepath);

        try {
            BufferedWriter bw= new BufferedWriter(new FileWriter(tempFile,true));
            PrintWriter pw = new PrintWriter(bw);

            FileReader fr= new FileReader(filepath);
            BufferedReader csv= new BufferedReader(fr);

            String currentLine= csv.readLine();
            // currentLine = csv.readLine();
            currentLine = csv.readLine();

            while((currentLine=csv.readLine())!=null)
            {
                String[] data=currentLine.split(csvsplit);
                if((data[0].equalsIgnoreCase(term)))
                {
                    data[newindex]=newterm;
                }
                StringBuilder sb= new StringBuilder();
                for(String s: data)
                {
                    sb.append(s).append(",");
                }
                String temp=sb.deleteCharAt(sb.length()-1).toString();
                pw.println(temp);
            }
            pw.flush();
            pw.close();
            fr.close();
            csv.close();
            bw.close();

            oldFile.delete();
            File dump= new File(filepath);
            newFile.renameTo(dump);
            JOptionPane.showMessageDialog(null, "Record changed");
        }
        catch (Exception e3)
        {
            JOptionPane.showMessageDialog(null,"Record NOT changed");
            e3.printStackTrace();
        }
    }

    public static void modifyRecordInterface(String mon)
    {
        JPanel panel = new JPanel();
        JFrame frame = new JFrame();
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(panel);

        panel.setLayout(null);

        JLabel rollLabel = new JLabel("Enter modifying roll no");
        rollLabel.setBounds(50, 50, 165, 25);
        panel.add(rollLabel);

        JTextField rollModify = new JTextField(10);
        rollModify.setBounds(200, 50, 165, 25);
        panel.add(rollModify);



        JButton modifyButton1 = new JButton("Modify Name");
        modifyButton1.setBounds(50, 100,80,25);
        panel.add(modifyButton1);
        modifyButton1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JPanel panel2 = new JPanel();
                JFrame frame2 = new JFrame();
                frame2.setSize(500, 500);
                frame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame2.add(panel2);

                panel2.setLayout(null);

                JLabel nameLabel = new JLabel("Enter new name");
                nameLabel.setBounds(50, 50, 165, 25);
                panel2.add(nameLabel);

                JTextField nameModify = new JTextField(10);
                nameModify.setBounds(200, 50, 165, 25);
                panel2.add(nameModify);

                JButton modifyButton = new JButton("Modify");
                modifyButton.setBounds(50, 100,80,25);
                panel2.add(modifyButton);
                modifyButton.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        String roll = rollModify.getText();
                        String name = nameModify.getText();
                        int newindex = 1;
                        TeacherInterface2.modifyRecord(newindex, roll, name, mon);
                    }
                });
                frame2.setVisible(true);
            }
        });
        JButton modifyButton2 = new JButton("Modify Attendance");
        modifyButton2.setBounds(50, 140,260,25);
        panel.add(modifyButton2);
        modifyButton2.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JPanel panel2 = new JPanel();
                JFrame frame2 = new JFrame();
                frame2.setSize(500, 500);
                frame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame2.add(panel2);

                panel2.setLayout(null);

                JLabel subjectLabel = new JLabel("Enter subject");
                subjectLabel.setBounds(50, 20, 165, 25);
                panel2.add(subjectLabel);

                JTextField subjectText = new JTextField(30);
                subjectText.setBounds(150, 20, 100, 25);
                panel2.add(subjectText);

                JLabel hrsLabel = new JLabel("Enter no of hours present");
                hrsLabel.setBounds(50, 50, 165, 25);
                panel2.add(hrsLabel);

                JTextField hrsText = new JTextField(10);
                hrsText.setBounds(150, 50, 80, 25);
                panel2.add(hrsText);

                JButton nextButton = new JButton("Next");
                nextButton.setBounds(100, 80, 80, 25);
                panel2.add(nextButton);
                nextButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        int subindex = -1;
                        String roll = rollModify.getText();
                        String subject = subjectText.getText();
                        String hrs = hrsText.getText();
                        for (int i = 0; i < numCourses; i++) {
                            if (courseName[i].equals(subject)) {
                                subindex = sindex[i];
                                break;
                            }
                        }
                        TeacherInterface2.modifyRecord(subindex, roll, hrs, mon);
                    }
                });
                frame2.setVisible(true);
            }
        });
        frame.setVisible(true);
    }
}


class TeacherInterface extends JFrame{

    TeacherInterface(){
        JPanel panel = new JPanel();
        JFrame frame = new JFrame();
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(panel);

        panel.setLayout(null);

        JLabel monLabel = new JLabel("Enter month");
        monLabel.setBounds(50, 20, 100, 25);
        panel.add(monLabel);

        JTextField monText = new JTextField(10);
        monText.setBounds(150, 20, 165, 25);
        panel.add(monText);

        JButton nextButton = new JButton("Next");
        nextButton.setBounds(50, 50,80,25);
        panel.add(nextButton);
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JPanel panel2 = new JPanel();
                JFrame frame2 = new JFrame();
                frame2.setSize(500, 500);
                frame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame2.add(panel2);

                panel2.setLayout(null);

                JLabel welcome = new JLabel("Welcome");
                welcome.setBounds(10, 20, 165, 25);
                panel2.add(welcome);



                JButton button1 = new JButton("Add a Student");
                button1.setBounds(10, 50, 200, 25);
                panel2.add(button1);
                button1.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String month = monText.getText();
                        TeacherInterface2.saveRecord(month);
                    }
                });

                JButton button2 = new JButton("Modify Student details ");
                button2.setBounds(10, 80, 200, 25);
                panel2.add(button2);
                button2.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String month = monText.getText();
                        TeacherInterface2.modifyRecordInterface(month);
                    }
                });

                JButton button3 = new JButton("Delete Student");
                button3.setBounds(10, 110, 200, 25);
                panel2.add(button3);
                button3.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String month = monText.getText();
                        TeacherInterface2.deleteRecord(month);
                    }
                });

                JButton button4 = new JButton("Attendance Report");
                button4.setBounds(10, 140, 200, 25);
                panel2.add(button4);
                button4.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JPanel panel3 = new JPanel();
                        JFrame frame3 = new JFrame();
                        frame3.setSize(500, 500);
                        frame3.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        frame3.add(panel3);

                        panel3.setLayout(null);

                        JLabel subLabel = new JLabel("Select course to view");
                        subLabel.setBounds(50, 20, 200, 25);
                        panel3.add(subLabel);

                        JButton laButton = new JButton("Linear Algebra");
                        laButton.setBounds(50, 50,200,25);
                        panel3.add(laButton);
                        laButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                String month = monText.getText();
                                Csvfile file= new Csvfile();
                                file.readFile(month, 0, "0");
                            }
                        });

                        JButton dsButton = new JButton("Data Structures");
                        dsButton.setBounds(50, 80,200,25);
                        panel3.add(dsButton);
                        dsButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                String month = monText.getText();
                                Csvfile file= new Csvfile();
                                file.readFile(month,1, "0");
                            }
                        });

                        JButton caButton = new JButton("Computer Architecture");
                        caButton.setBounds(50, 110,200,25);
                        panel3.add(caButton);
                        caButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                String month = monText.getText();
                                Csvfile file= new Csvfile();
                                file.readFile(month, 2, "0");
                            }
                        });

                        JButton discrButton = new JButton("Discrete Structures");
                        discrButton.setBounds(50, 140,200,25);
                        panel3.add(discrButton);
                        discrButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                String month = monText.getText();
                                Csvfile file= new Csvfile();
                                file.readFile(month, 3, "0");
                            }
                        });

                        JButton oopButton = new JButton("OOP");
                        oopButton.setBounds(50, 170,200,25);
                        panel3.add(oopButton);
                        oopButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                String month = monText.getText();
                                Csvfile file= new Csvfile();
                                file.readFile(month, 4, "0");
                            }
                        });

                        JButton ecoButton = new JButton("Economics for Engineers");
                        ecoButton.setBounds(50, 200,200,25);
                        panel3.add(ecoButton);
                        ecoButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                String month = monText.getText();
                                Csvfile file= new Csvfile();
                                file.readFile(month, 5, "0");
                            }
                        });

                        JButton dslButton = new JButton("Data Structures Laboratory");
                        dslButton.setBounds(50, 230,200,25);
                        panel3.add(dslButton);
                        dslButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                String month = monText.getText();
                                Csvfile file= new Csvfile();
                                file.readFile(month, 6, "0");
                            }
                        });

                        JButton ooplButton = new JButton("OOP Laboratory");
                        ooplButton.setBounds(50, 260,200,25);
                        panel3.add(ooplButton);
                        ooplButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                String month = monText.getText();
                                Csvfile file= new Csvfile();
                                file.readFile(month, 7, "0");
                            }
                        });

                        JButton evsButton = new JButton("Environmental Science");
                        evsButton.setBounds(50, 290,200,25);
                        panel3.add(evsButton);
                        evsButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                String month = monText.getText();
                                Csvfile file= new Csvfile();
                                file.readFile(month, 8, "0");
                            }
                        });
                        frame3.setVisible(true);
                    }
                });
                frame2.setVisible(true);
            }
        });
        frame.setVisible(true);

    }
}

class Main extends JFrame {
    public static void main(String args[])
    {
        JPanel panel = new JPanel();
        JFrame frame = new JFrame();
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        panel.setLayout(null);

        JLabel title = new JLabel("Attendance Tracker");
        title.setBounds(100, 20, 165, 25);
        panel.add(title);

        JLabel userLabel = new JLabel("ID");
        userLabel.setBounds(50, 50, 80, 25);
        panel.add(userLabel);

        JTextField userText = new JTextField(20);
        userText.setBounds(150, 50, 165, 25);
        panel.add(userText);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(50, 80, 80, 25);
        panel.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(150, 80, 165, 25);
        panel.add(passwordText);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(50, 110,80,25);
        panel.add(loginButton);
        loginButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                String user = userText.getText();
                String password = passwordText.getText();

                if (user.equals("faculty") && password.equals("psgtech")) {
                    new TeacherInterface();
                } else if (user.equals(RollNum.check_roll(user)) && password.equals(user + user)) {
                    new StudentInterface(user);
                }else{
                    userText.setText("");
                    passwordText.setText("");
                    JOptionPane.showMessageDialog(null,"Incorrect user name or password");
                }
            }
        });
        frame.setVisible(true);
    }
}
