import java.util.*;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;

import java.awt.*;
import java.awt.event.*;

class LifeAlign extends JFrame
{
  private int arriveTime;
  private int burstTime;
  private int priority;
  private String identity; // process number(eg. P1)
  private int turnaround; // time taken to submit and completion of our operation
  private int waiting;
  private int finishTime;  // process finish scheduling time
  private boolean status;  // to know that process been scheduling already or not
   
  public LifeAlign(String input1,int input2, int input3, int input4, boolean input5 )
  {
      identity = input1;
      setArriveTime(input2);
      setBurstTime(input3);
      setPriority(input4);
      setStatus(input5);
  }

  public LifeAlign()
  {
     JFrame frame = new JFrame("LifeAlign");
     String[] choice = {"3","4","5","6","7","8","9","10"};
     JComboBox<String> combo = new JComboBox<String>(choice);
     JPanel panel = new JPanel();
     JLabel label = new JLabel("Select amount of Tasks that you want to do");

     ActionListener ActionListener = new ActionListener() // after user choose how many process he/she want, let user insert input
     {
         public void actionPerformed(ActionEvent e)
         {
          int index=1;
          JFrame frame2 = new JFrame("Kindly, enter your hectic routine to be simplified");
          JPanel panel2 = new JPanel();
          String item = (String)combo.getSelectedItem();
          int number = Integer.parseInt(item)+1;
          JTable table = new JTable(number,4);
          JButton btn = new JButton("Compute");
          table.setValueAt("Enter Tasks",0,0);
          table.setValueAt("Start Time",0,1);
          table.setValueAt("Duration",0,2);
          table.setValueAt("Set Priority",0,3);

          for(int i=1; i<number; i++)
          {
              table.setValueAt(index,i,0); // set each process initial identity(process number)
              table.setValueAt(0,i,3); // set 0 for each cell of priority
              index++;
          }

          ActionListener btnActionListener = new ActionListener() // start LifeAlign after user click "LifeAlign" button
          {
              public void actionPerformed(ActionEvent e)
              {
                  ArrayList<LifeAlign> initialProcess = new ArrayList<LifeAlign>();
                  ArrayList<LifeAlign> initialProcess2 = new ArrayList<LifeAlign>();
                  ArrayList<LifeAlign> initialProcess3 = new ArrayList<LifeAlign>();
                  ArrayList<LifeAlign> priorityResult = new ArrayList<LifeAlign>();
                  for(int i=1; i<number; i++) //Retrieve each value in the table and put into arrayList<LifeAlign>
                  {
                     String task = (String)table.getValueAt(i,0);
                     Object l = table.getValueAt(i,1);
                     Object k = table.getValueAt(i,2);
                     Object j = table.getValueAt(i,3);
                     try{    
                     int at = Integer.parseInt(l.toString());
                     int bt = Integer.parseInt(k.toString());
                     int pt = Integer.parseInt(j.toString());
                     initialProcess.add(new LifeAlign(task,at,bt,pt,false));
                     initialProcess2.add(new LifeAlign(task,at,bt,pt,false));
                     initialProcess3.add(new LifeAlign(task,at,bt,pt,false));
                    }catch(NullPointerException ext)  //if user enter null value, throw the error
                    {
                        JDialog dialog = new JDialog(frame2,"Error!");
                        JPanel errorPanel = new JPanel();
                        JLabel errorMessage = new JLabel("No data entered, please check again");
                        errorPanel.add(errorMessage);
                        dialog.add(errorPanel);
                        dialog.setSize(350,100);
                        dialog.setVisible(true);
                        dialog.setResizable(false);
                        break;
                    }
                  }

                  priorityResult = getPriority(number-1,initialProcess); // call the function to calculate non preemptive priority scheduling
                  JFrame frame3 = new JFrame("Here is your life aligned");      
                  JTable ganttChart1 = new JTable(2,priorityResult.size()+1);   // create table for show result after scheduling
                  int rol=0;
                  int col=0;
                  for(int i=0; i<priorityResult.size(); i++) //loop to set the values of each cells in gantt chart(priority)
                  {
                      ganttChart1.setValueAt(priorityResult.get(i).getIdentity(),rol,col);  // set process number according arrangement
                      //set each process's start time and finish time
                      if(i==0) 
                      {
                          ganttChart1.setValueAt(priorityResult.get(0).getArriveTime(),rol+1,col); 
                        
                      }
                        else 
                        {
                            ganttChart1.setValueAt(priorityResult.get(i-1).getFinishTime(),rol+1,col); 
                            
                            if(i==priorityResult.size()-1)
                            {
                                ganttChart1.setValueAt(priorityResult.get(i).getFinishTime(),rol+1,col+1);
    
                            }
                        }
                      col++;
                  } 
                  rol=0;
                  col=0;
                  
                  HashMap<String,String> eachProcessTurn = new HashMap<String,String>(); 
                  HashMap<String,String> eachProcessWait = new HashMap<String,String>();
                  for(LifeAlign xx: priorityResult) // Store Waiting time and turnaround time of each process(non premptive priority) 
                  {
                      eachProcessTurn.put(xx.getIdentity(),String.valueOf(xx.getTurnaround()));
                      eachProcessWait.put(xx.getIdentity(),String.valueOf(xx.getWaiting()));
                  }
                  
                  HashMap<String,String> eachProcessTurn2 = new HashMap<String,String>();
                  HashMap<String,String> eachProcessWait2 = new HashMap<String,String>();
                  ArrayList<LifeAlign> check = new ArrayList<LifeAlign>();
                 
                  
                  for(LifeAlign xx: check) //Store Waiting time and turnaround time of each process(premptive SJF)
                  {
                      eachProcessTurn2.put(xx.getIdentity(),String.valueOf(xx.getTurnaround()));
                      eachProcessWait2.put(xx.getIdentity(),String.valueOf(xx.getWaiting()));
                  }
                  
                  HashMap<String,String> eachProcessTurn3 = new HashMap<String,String>();
                  HashMap<String,String> eachProcessWait3 = new HashMap<String,String>();
                  
                  
                  int priorityTotalTurn = getSumTurn(priorityResult); //get total turnaround time
                
                  float priorityAVGTurn = getAvgTurn(priorityResult,number-1); //get average turnaround time
                  
                  int priorityTotalWait = getSumWait(priorityResult); //get total waiting time
                 
                  float priorityAVGWait = getAvgWait(priorityResult, number-1);  //get average waiting time
                 
                  JPanel gridBoard = new JPanel(new GridLayout(3,0,5,5));
                  JPanel board = new JPanel(new BorderLayout());
                  JButton calculate = new JButton("Calculation");
                  ActionListener calListener = new ActionListener() //call the frame to show calculation for each LifeAlign algorithms
                  {
                      public void actionPerformed(ActionEvent cal)
                      {
                          JFrame frame4 = new JFrame("LifeAlign");
                          JPanel calGridPanel = new JPanel(new GridLayout(3,0,5,5));
                          JTextArea calresult1 = new JTextArea("1.Non Preemptive Priority LifeAlign Total Turnaround Time:" + priorityTotalTurn + "\n" +"2.Non Preemptive Priority LifeAlign Average Turnaround Time:" + priorityAVGTurn + "\n" + "3.Non Preemptive Priority LifeAlign Total Waiting Time:" + priorityTotalWait + "\n" + "4.Non Preemptive Priority LifeAlign Average Waiting Time:" + priorityAVGWait + "\n" + "5.Each Process Turnaround time:" + eachProcessTurn + "\n" + "6.Each Process Waiting Time:" + eachProcessWait + "\n");
                          calGridPanel.add(calresult1);
                          frame4.add(calGridPanel);
                          frame4.setSize(1000,1000);
                          frame4.setVisible(true);
                      }
                  };
                  calculate.addActionListener(calListener);
                  JLabel text = new JLabel("Non Preemptive Priority LifeAlign");
                  board.add(text,"Center");
                  board.add(ganttChart1,"Center");
                  gridBoard.add(board);
                  frame3.add(gridBoard);
                  frame3.setSize(500,250);
                  frame3.setVisible(true);
              }
          };
          btn.addActionListener(btnActionListener);
          panel2.add(table);
          panel2.add(btn);
          frame2.add(panel2);
          frame2.setSize(500,500);
          frame2.setVisible(true);
          frame2.setResizable(false);
         }
     };
     combo.addActionListener(ActionListener);
     panel.add(combo);
     panel.add(label);
     frame.add(panel);
     frame.setSize(500,500);
     frame.setVisible(true);
     frame.setResizable(false);
  }
  
  public void setArriveTime(int arrive)   //set the value of Arrive time for that process
  {
      arriveTime = arrive;
  }
    
  public int getArriveTime() //get the value of Arrive time for that process
  {
      return arriveTime;
  }
  
  public void setBurstTime(int burst)
  {
      burstTime = burst;
  }
  
  public int getBurstTime()
  {
      return burstTime;
  }
  
  public void setPriority(int pr)
  {
      priority = pr;
  }
  
  public int getPriority()
  {
      return priority;
  }
  
  public void setStatus(boolean s) //set the value of status for that process
  {
      status = s;
  }
    
  public boolean getStatus() //get the value of status for that process
  {
      return status;
  }
  
  public String getIdentity() 
  {
      return identity;
  }
  
  public void setTurnaround(int setT)
  {
      turnaround = setT;
  }
  
  public int getTurnaround()
  {
      return turnaround;
  }
  
  public void setWaiting(int setW)
  {
      waiting = setW;
  }
  
  public int getWaiting() 
  {
      return waiting; 
  }
  
    public void setFinishTime(int setF)
  {
      finishTime = setF;
  }
  
  public int getFinishTime()
  {
      return finishTime;
  }

  public int getSumTurn(ArrayList<LifeAlign> result1)  
  {
     int sumTurnaround=0;
     for(LifeAlign i: result1) 
     {
        sumTurnaround += i.getTurnaround();
     }     
     return sumTurnaround;
  }

  public int getSumWait(ArrayList<LifeAlign> result2) 
  {
     int sumWaiting=0;
     for(LifeAlign i: result2) 
     {
        sumWaiting += i.getWaiting();
     }  
     return sumWaiting;
  }
  
  public float getAvgTurn(ArrayList<LifeAlign> result3, int num) 
  {
      float sumT = getSumTurn(result3);
      float sumW = getSumWait(result3);
      float avgTurn = sumT/num;
      return avgTurn;
  }
  
  public float getAvgWait(ArrayList<LifeAlign> result4, int num) 
  {
      float sumW = getSumWait(result4);
      float avgWait = sumW/num;
      return avgWait;
  }
  
  /**
     Non premptive priority LifeAlign result
   * totalProcess == number of process user want
   * initialProcess == input that user enter for each process
   */
  public ArrayList<LifeAlign> getPriority(int totalProcess, ArrayList<LifeAlign> initialProcess) 
  {
      ArrayList<LifeAlign> newProcess = new ArrayList<LifeAlign>();
      ArrayList<LifeAlign> checkProcess = new ArrayList<LifeAlign>();
      ArrayList<LifeAlign> oldProcess = new ArrayList<LifeAlign>();
      oldProcess = initialProcess;
      int processNum = totalProcess;
      LifeAlign temp;
      
      for(int i=0; i<processNum; i++) //to find which process is the first process
     {
         for(int j=0; j<processNum-i-1; j++)
         {
             if(oldProcess.get(j).getArriveTime()> oldProcess.get(j+1).getArriveTime()) //swap according arrive time
             {
              temp = oldProcess.get(j);
              oldProcess.set(j,oldProcess.get(j+1));
              oldProcess.set(j+1,temp);
             }
             
             else if(oldProcess.get(j).getArriveTime() == oldProcess.get(j+1).getArriveTime()) 
             {
              if(oldProcess.get(j).getPriority() > oldProcess.get(j+1).getPriority()) //swap according priority if both arrive time same
              {
              temp = oldProcess.get(j);
              oldProcess.set(j,oldProcess.get(j+1));
              oldProcess.set(j+1,temp);
              }
              
              else if(oldProcess.get(j).getPriority()==oldProcess.get(j+1).getPriority())
              {
                  if(oldProcess.get(j).getBurstTime()>oldProcess.get(j+1).getBurstTime())//swap according burst time if priority and arrive time same
                  {
                   temp = oldProcess.get(j);
                   oldProcess.set(j,oldProcess.get(j+1));
                   oldProcess.set(j+1,temp);
                }
              }
            }
        }   
     }
     newProcess.add(oldProcess.get(0));  //successfully find the first process been execute and put into newProcess for showing final result 
     int timer=0; 
     
     // set timer value as first process finish time 
     if(oldProcess.get(0).getArriveTime() != 0)  
     {
         timer = timer + oldProcess.get(0).getArriveTime() + oldProcess.get(0).getBurstTime(); 
     }
     else
     {
         timer = oldProcess.get(0).getBurstTime(); 
     }
     
     oldProcess.remove(0); //remove finished shceduling process
     newProcess.get(0).setFinishTime(timer);
     int index =1;
      for(int a=0; a<processNum-1; a++)
     {
         for(int b=0; b<oldProcess.size(); b++) //check which process arrived after first process finish executed
         {
             if(oldProcess.get(b).getArriveTime()<timer) 
             {
                 checkProcess.add(oldProcess.get(b)); //put arrived process in checkProcess for further LifeAlign
             }
             else
             {
                 continue;
             }
         }
         
         //if has not any process arrived after first process finish executed, put shortest arrive time process into checkProcess
         if(checkProcess.size()==0) 
         {
             for(int i=0; i<oldProcess.size(); i++)
             {
               checkProcess.add(oldProcess.get(0));
             }
         }
         
         //LifeAlign further process
         for(int c=0; c<checkProcess.size();c++)
         {
             for(int d=0; d<checkProcess.size()-1-c;d++)
             {
                 if(checkProcess.get(d).getPriority()>checkProcess.get(d+1).getPriority())
                 {
                     temp = checkProcess.get(d);
                     checkProcess.set(d,checkProcess.get(d+1));
                     checkProcess.set(d+1,temp);        
                 }
                 
                 else if(checkProcess.get(d).getPriority()==checkProcess.get(d+1).getPriority())
                 {
                     if(checkProcess.get(d).getBurstTime()>checkProcess.get(d+1).getBurstTime())
                     {
                         temp = checkProcess.get(d);
                         checkProcess.set(d,checkProcess.get(d+1));
                         checkProcess.set(d+1,temp);
                     }
                 }
             }
         }
         newProcess.add(checkProcess.get(0));  //put finished LifeAlign process 
         oldProcess.remove(checkProcess.get(0)); //remove process that finished LifeAlign
         timer = timer + checkProcess.get(0).getBurstTime();
         newProcess.get(index).setFinishTime(timer);
         checkProcess.clear(); 
         index++;
     }
      
     index = 0; 
     for(LifeAlign i: newProcess) //find each process turnaround time,waiting time 
     {
        i.setTurnaround((i.getFinishTime() - i.getArriveTime()));
        i.setWaiting(i.getTurnaround()-i.getBurstTime());
        index++;
     }  

     return newProcess;
  }

public static void main(String args[])
  {
      new LifeAlign();
  }
}