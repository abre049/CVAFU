/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cvafu;

import java.awt.Color;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Set;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Font;
import java.awt.RenderingHints;
import java.awt.BasicStroke;
import java.util.Collections;
import java.awt.geom.Rectangle2D;
import java.time.temporal.ChronoUnit;

/**
 *
 * @author Alex
 */
public class Patient implements Serializable{
    
    String nhi;
    String name = "default";
    int age;
    LocalDate admitDate;
    LocalDate ophrsDate;
    LocalDate dcDate;
    List<LocalDate> drs = new ArrayList<LocalDate>();
    List<LocalDate> pt = new ArrayList<LocalDate>();
    List<LocalDate> ot = new ArrayList<LocalDate>();
    List<LocalDate> slt = new ArrayList<LocalDate>();
    List<LocalDate> nrs = new ArrayList<LocalDate>();
    Hashtable<LocalDate, Integer> mr = new Hashtable<LocalDate, Integer>();
    String notes;
    Housing homePre;
    Housing homePost;
    
    final Hashtable<Integer, Color> MR_COLORS = new Hashtable<Integer, Color>(){{
        put(0, new Color(187, 205, 230));
        put(1, new Color(172, 180, 213));
        put(2, new Color(139, 156, 197));
        put(3, new Color(123, 148, 197));
        put(4, new Color(90, 115, 172));
        put(5, new Color(65, 90, 131));
        put(6, new Color(255,0,0));
    }};
    
    
    public Patient (String name, String nhi, int age){
        this.nhi = nhi;
        this.name = name;
        this.age = age;
    }
    
    public void updateAdmitDate(LocalDate date){
        this.admitDate = date;
    }
    public void updateOphrsDate(LocalDate date){
        this.ophrsDate = date;
    }
    public void updateDcDate(LocalDate date){
//        System.out.println("here");
        this.dcDate = date;
    }
    public void addMr(int mrScore, LocalDate mrDate){
        this.mr.put(mrDate, mrScore);
    }
    public void removeMr(LocalDate mrDate){
        this.mr.remove(mrDate);
    }
    public List getMrDates(){
        List<LocalDate> dates = new ArrayList<LocalDate>();
        if (!mr.isEmpty()){
            dates.addAll(mr.keySet());
        }
        return dates;
    }
    public int getMrScore(LocalDate date){
        int score;
        score = mr.get(date);
        return score;
    }
    
    public void print(){
        System.out.println("name: " + name);
        System.out.println("nhi: " + nhi);
        System.out.println("age: " + age);
        System.out.println("admission: " + admitDate);
        System.out.println("ophrs: " + ophrsDate);
        System.out.println("dc: " + dcDate);
        if (!pt.isEmpty()){
            System.out.print("pt: ");
            for (Object d : pt){
                System.out.println(d.toString());}
        }
        if (!ot.isEmpty()){
            System.out.print("ot: ");
            for (Object d : ot){
                System.out.println(d.toString());}
        }
        if (!slt.isEmpty()){
            System.out.print("slt: ");
            for (Object d : slt){
                System.out.println(d.toString());}
        }
        if (!drs.isEmpty()){
            System.out.print("drs: ");
            for (Object d : drs){
                System.out.println(d.toString());}
        }
        if (!nrs.isEmpty()){
            System.out.print("nrs: ");
            for (Object d : nrs){
                System.out.println(d.toString());}
        }
        
        if (!mr.isEmpty()){
            System.out.print("mr: ");
            List<LocalDate> dates = new ArrayList<LocalDate>(mr.keySet());
            for (LocalDate d : dates){
                System.out.print(d.toString());
                System.out.print(": ");
                System.out.println(mr.get(d));
            }
        }
        
        System.out.println("Notes: " + notes);
        
    }
    
    public BufferedImage makeImage(){
        final int STROKE_Y1 = 128;
        final int STROKE_Y2 = 384;
        final double MR_Y1 = STROKE_Y1 + ((STROKE_Y2 - STROKE_Y1)*2/5);
        final double MR_Y2 = STROKE_Y1 + ((STROKE_Y2 - STROKE_Y1)*3/5);
        final int MR_SCORE_Y = ((int) ((MR_Y2+MR_Y1)/2)) + 10;
        
        BufferedImage bimg = new BufferedImage(1024,512,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bimg.createGraphics();
        g2.setBackground(Color.white);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        //draw name
        Font font = new Font("Serif", Font.PLAIN, 50);
        g2.setFont(font);
        g2.setPaint(Color.black);
        String string = name + " " + age;
        g2.drawString(string, 230, 80);
        
        //draw home
        
        //draw MR
        List<LocalDate> dates = new ArrayList<LocalDate>(mr.keySet());
        Collections.sort(dates);
        Collections.reverse(dates);
        LocalDate prevDate = null;
        for (LocalDate date : dates){
            if (date.isBefore(admitDate)){
                Rectangle2D rect = new Rectangle2D.Double(100, MR_Y1, 100, (MR_Y2-MR_Y1));
                g2.setPaint(MR_COLORS.get(mr.get(date)));
                g2.fill(rect);
                g2.setPaint(Color.black);
                g2.setFont(new Font("Serif", Font.PLAIN, 30));
                g2.drawString(Integer.toString(mr.get(date)), 110, MR_SCORE_Y);
            } else if (ChronoUnit.DAYS.between(admitDate, date)<=365){
                if (prevDate == null){
                    int daysBetween = (int) ChronoUnit.DAYS.between(admitDate, date);
                    int x = daysToPix(daysBetween);
                    Rectangle2D rect = new Rectangle2D.Double(x, MR_Y1, (365-daysBetween)*2, (MR_Y2-MR_Y1));
                    g2.setPaint(MR_COLORS.get(mr.get(date)));
                    g2.fill(rect);
                    g2.setPaint(Color.black);
                    g2.setFont(new Font("Serif", Font.PLAIN, 30));
                    g2.drawString(Integer.toString(mr.get(date)), x+10, MR_SCORE_Y);
                    prevDate = date;
                } else {
                    int daysBetween = (int) ChronoUnit.DAYS.between(date, prevDate);
                    int x = daysToPix((int) ChronoUnit.DAYS.between(admitDate, date));
                    Rectangle2D rect = new Rectangle2D.Double(x, MR_Y1, daysBetween*2, (MR_Y2-MR_Y1));
                    g2.setPaint(MR_COLORS.get(mr.get(date)));
                    g2.fill(rect);
                    g2.setPaint(Color.black);
                    g2.setFont(new Font("Serif", Font.PLAIN, 30));
                    g2.drawString(Integer.toString(mr.get(date)), x+10, MR_SCORE_Y);

                    prevDate = date;
                }
            }
        }
        
        //draw admissions
        int daysBetween = (int) ChronoUnit.DAYS.between(admitDate, ophrsDate);
        Rectangle2D rect = new Rectangle2D.Double(daysToPix(0), MR_Y2, daysBetween*2, (MR_Y2-MR_Y1));
        g2.setPaint(Color.cyan);
        g2.fill(rect);
        
        int x1 = daysToPix(daysBetween);
        daysBetween = (int) ChronoUnit.DAYS.between(ophrsDate, dcDate);
        rect = new Rectangle2D.Double(x1, MR_Y2, daysBetween*2, (MR_Y2-MR_Y1));
        g2.setPaint(Color.green);
        g2.fill(rect);

        
        
        //draw visits
//        List<List> mdt = new ArrayList<List>();
//        mdt.add(this.pt);
//        mdt.add(this.ot);
//        mdt.add(this.slt);
//        mdt.add(this.drs);
//        mdt.add(this.nrs);
//        for (List list : mdt){
//            for (Object date : list){
//                drawArrow(LocalDate date, mdt.get(list))
//            }
//        }
        
        
        
        //draw stroke line
        g2.setPaint(Color.red);
        g2.setStroke(new BasicStroke(5));
        g2.drawLine(daysToPix(0), STROKE_Y1, daysToPix(0), STROKE_Y2);
        
        //draw notes
        
        return bimg;
    }
    
    private Integer daysToPix(Integer day){
        int xpix;
        int day0 = 200;
        int pixPerDay = 2;
        xpix = day0 + (pixPerDay * day);
        return xpix;
    }
    
    
    public enum Housing {
        ALONE("Home Alone"),
        SUPPORT("Home with Support"),
        RESTHOME("Rest Home"),
        HOSPITAL("Private Hospital");
        
        private String homeStr;
        
        private Housing(String homeStr){
            this.homeStr = homeStr;
        }
            
        public String getString(){
            return this.homeStr;
        }
        public static List getAllStrings(){
            List list = new ArrayList();
            for (Housing h : Housing.values()){
                list.add(h.getString());
            }
            return list;
        }
        public static Housing getHousing(String string){
            Housing returnH = null;
            for (Housing h : Housing.values()){
                if (h.getString() == string){
                    returnH = h;
                }
            }
            return returnH;
        }
    }
}
