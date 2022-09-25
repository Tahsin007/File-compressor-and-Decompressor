
package HuffmanEncoder;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import static lzw.Encoder.LZWfilename;


/**
 *
 * @author Tahsin
 */
public class HuffmanEncode {

    public static HashMap<Character,String> encoder;
    public static int frequencycount[];
    public static int totalFrequency = 0;//No. of characters in original file
    public static int totalEncoded = 0;//No. of characters in encoded file
    public String sourcePath = "C:\\Users\\TANIM\\Desktop\\Seo.txt";
    public String destinationPath = "C:\\Users\\TANIM\\Desktop\\3rd Semister\\Networking";
    public String saveFile;
    
    
    public static void main(String[] args) throws IOException {

        HuffmanEncode ob = new HuffmanEncode();
        //dc.countFrequency();
        Huffman root = ob.encode();
        //ob.generateEncodedBitFile("C:\\Users\\TANIM\\Desktop\\Seo.txt","C:\\Users\\TANIM\\Desktop\\3rd Semister\\Networking");
        //dc.generateEncodedFile("C:\\Users\\TANIM\\Desktop\\Seo.txt","C:\\Users\\TANIM\\Desktop\\3rd Semister\\Networking");
    }

    public void countFrequency(String sourcePath)
    {
        frequencycount = new int[256];        
        try(FileReader fr = new FileReader(sourcePath))
        {
            int c;           
            while((c = fr.read())!= -1)
                if(c<256)
                {
                    frequencycount[c]++;
                    totalFrequency++;
                }            
        }
        catch(IOException e)
        {
            System.out.println("IO error :"+e);
        }
    }
    /*
        Sets up the min-Heap
    */
    public Huffman encode()
    {
        int n = frequencycount.length;
        
        PriorityQueue <Huffman> minheap= new PriorityQueue<>(n,FREQUENCY_COMPARATOR);
        
        char c;int a;
        for(int i=0;i<n;i++)
        {
            if(frequencycount[i]!=0)
                minheap.add(new Huffman((char)i,frequencycount[i]));
        }
        
        Huffman z=null;
        while(minheap.size()>1)
        {
            Huffman x=minheap.peek();
            minheap.poll();
            Huffman y=minheap.peek();
            minheap.poll();
            x.setCode("0");
            y.setCode("1");
            z=new Huffman();
            z.setCharacter('\u09B8');
            z.setFrequency(x.getFrequency()+y.getFrequency());
            z.setleftChild(x);
            z.setrightChild(y);
            //System.out.println(z.getFrequency());
            minheap.add(z);            
        }   
        encoder = new HashMap<>();
        Huffman root=z;
        traverse(root,"");
        System.out.println("");
        for(int i=0;i<n;i++)
           if(frequencycount[i]!=0)
            System.out.println((char)i+"\t"+frequencycount[i]+"\t"+encoder.get((char)i));
        return root;
    }
   
    public static final Comparator<Huffman> FREQUENCY_COMPARATOR = (Huffman o1, Huffman o2) -> (int) (o1.getFrequency()-o2.getFrequency());

    public void traverse(Huffman root,String s)throws NullPointerException
    {   
        if(root.getCode()!=null)
            s+=root.getCode();
        //Leaf Node Case
        if(root.getleftChild()==null && root.getrightChild()==null && root.getCharacter()!='\u09B8')
            {
                System.out.println(root.getCharacter()+":"+s);
                encoder.put(root.getCharacter(), s);
                return;
            }
        
         traverse(root.getleftChild(), s);
         traverse(root.getrightChild(), s);
    }
    //@SuppressWarnings("empty-statement")
    public void generateEncodedBitFile(String sourcePath,String saveFile) throws IOException
    {
        System.out.println("Contents");
        ArrayList<Byte> contentByte = new ArrayList<>();  
        StringBuffer contents=new StringBuffer();                
        try(FileInputStream fis = new FileInputStream(sourcePath);)
        {
            int c;
            while((c = fis.read())!= -1)
                if(c<256)
                {
                    
                    contents.append(encoder.get((char)c));  
                    System.out.print(encoder.get((char)c)+",");//Integer.parseInt(g,2)
                    
                    //catch(StringIndexOutOfBoundsException ae){;}
                }
               
               
        }
        catch(IOException e)
        {
            System.out.println("IO error :"+e);
        }
        
        try
        {
            byte b=0;
            System.out.println("\n Contents:"+contents);
            int len;
            len=contents.length();
            
            if(len%8!=0){
                
                //System.out.println("Modified1:"+contents);
                int x=8-(len%8);
                for(int i=0;i<x;i++){
                    System.out.println(len);
                    contents.append('0');
                }
                //System.out.println("Modified2:"+contents);
            }
            
            for(int i=0;i<contents.length();i+=8){
                int j = i;String c="";
                while(j<i+8){
                    c+=contents.charAt(j++);
                }
                int decimalValue = Integer.parseInt(c, 2);
                contentByte.add((byte)b);
                System.out.println("C:"+decimalValue);
            }
            System.out.println(contents);
            //System.out.println("Modified:"+contents);
        }
        catch(NullPointerException ne){;} 
        //try(FileOutputStream fos = new FileOutputStream(destinationPath+"\\encodedbit.txt");)
        
        try(FileOutputStream fos = new FileOutputStream(saveFile);)
        {   
            for(byte b:contentByte)
            {
                
                fos.write(b);
            }
        } 
        catch (IOException ex) {
            Logger.getLogger(HuffmanEncode.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       System.out.println("Outside abyss");
    }
    
    
    public void generateEncodedFile(String sourcePath,String saveFile)
    {
        StringBuffer contents=new StringBuffer();
        
        try(FileReader fr = new FileReader(sourcePath);)
        {
            int c;
            while((c = fr.read())!= -1)
                if(c<256)
                    contents.append(encoder.get((char)c));  
        }
        catch(IOException e)
        {
            System.out.println("IO error :"+e);
        }
        //System.out.println(contents.length()/8);
        String save = saveFile.substring(0,saveFile.indexOf(".")) + "_bitFile.txt";
        //try(FileWriter fw = new FileWriter(destinationPath+"\\encoded.txt");)
        
        try(FileWriter fw = new FileWriter(save);)
        {
            fw.write(contents.toString());
        } catch (IOException ex) {
            Logger.getLogger(HuffmanDecoder.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       
    }
    
}
