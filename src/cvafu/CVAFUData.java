/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cvafu;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 *
 * @author Alex
 */
public class CVAFUData implements Serializable{
    
    private List<Patient> data = new ArrayList<Patient>();
    
    public void main(String[] args){
//        Patient p1 = addPatient("ted", "MJG4568", 68);
//        print();
//        p1.updateDcDate(new Date());
//        System.out.println(p1.dcDate);
    }
    
    public Patient addPatient(String name, String nhi, int age){
        Patient patient = new Patient(name, nhi, age);
        data.add(patient);
        return patient;
//        data.add(pt);
//        System.out.print(data.get(0).name);
    }
    
    public void save(){
        try{

            FileOutputStream fout = new FileOutputStream(".\\data.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(data);
            oos.close();
            System.out.println("Saved");

	   }catch(Exception ex){
		   ex.printStackTrace();
	   }
    }
    
    public void load(){
        try{

                FileInputStream fin = new FileInputStream(".\\data.ser");
                ObjectInputStream ois = new ObjectInputStream(fin);
                data = (ArrayList) ois.readObject();
                ois.close();
                System.out.println("Loaded");
//                return data;

	   }catch(Exception ex){
		   ex.printStackTrace();
//		   return null;
	   }
    }
    
    public void print(){
        for (Patient pt : data){
            pt.print();
        }
        
    }
    
    public List getNHIs(){
        List nhis = new ArrayList();
        for (Patient pt : data){
            nhis.add(pt.nhi);}
        return nhis;
    }
    public List getNames(){
        List names = new ArrayList();
        for (Patient pt : data){
            names.add(pt.name);}
        return names;
    }
    
    public Patient getPatientByName(String name){
        Patient patient = null;
        for (Patient pt : data){
            if (pt.name == name){
                patient = pt;
            }
        }
        return patient;
    }
    
    public Patient getPatientByNHI(String nhi){
        Patient patient = null;
        for (Patient pt : data){
            if (pt.nhi == nhi){
                patient = pt;
            }
        }
        return patient;
    }
}