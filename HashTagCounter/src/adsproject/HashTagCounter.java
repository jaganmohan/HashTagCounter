package adsproject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;

import adsproject.FibonacciHeap;
import adsproject.NodeFibHeap;

/**
 * Reads hashtags from an input file and outputs the top 'N' hashtags
 * when it encounters a request for top hashtags
 * @author Jagan
 */
public class HashTagCounter {
	
	//stores unique hashtags in a map
	private HashMap<String, NodeFibHeap> nodes = new HashMap<String, NodeFibHeap>();
	
	//using stack to temporarily store removed nodes
	private ArrayList<NodeFibHeap> stack = new ArrayList<NodeFibHeap>();
	
	//Fibonacci heap to perform orperation on the hashtags
	private FibonacciHeap heap = new FibonacciHeap();
	
	/**
	 * Inserts node representing a hashtag
	 * @param node node to be inserted
	 */
	public void insert(NodeFibHeap node){
		insert(node.key, node.data);
	}
	/**
	 * Inserts a hashtag into Fibonacci heap by calling @FibonacciHeap.insert method
	 * @param hashtag new tag to insert into heap
	 * @param data initial count of the hashtag
	 */
	public void insert(String hashtag, int data){
		NodeFibHeap temp = nodes.get(hashtag);
		if(temp != null){
			increase(hashtag, data);
		}else{
			NodeFibHeap node = new NodeFibHeap(hashtag, data);
			nodes.put(hashtag, node);
			heap.insert(node);
		}
	}
	
	/**
	 *Temporary remove  of top 'N' hashtags for outputting trending hashtags  
	 * @return top hashtag
	 * @throws Exception when trying to remove a node when heap is empty
	 */
	public NodeFibHeap remove() throws Exception{
		NodeFibHeap node = heap.remove();
		stack.add(node);
		nodes.remove(node.key);
		return node;
	}
	
	/**
	 * Increases the count of hashtags on encountering same hashtag
	 * @param hashtag hashtag whose value needs to be increased
	 * @param increase increase value
	 */
	public void increase(String hashtag, int increase){
		heap.increaseKey(nodes.get(hashtag), increase);
	}
	
	/**
	 * Add back the nodes removed while printing top hashtags
	 */
	public void addBackRemovedNodes(){
		for(NodeFibHeap node : stack){
			insert(node);
		}
		stack.clear();
	}
	
	/**
	 * Traverses through each line from input file
	 * @param list takes list which contains lines from an input file
	 * @param printw PrintWriter object to write output to file
	 */
	public void traverse(ArrayList<String> list, PrintWriter printw){
		
		for(String line : list){
			
			String arr[] = line.split(" ");
			if(arr[0].startsWith("#")){
				insert(arr[0].substring(1), Integer.parseInt(arr[1]));
			}else if(arr[0].matches("^[0-9]+$")){
				int i = Integer.parseInt(arr[0]), j = 0;
				StringBuilder output = new StringBuilder("");
				while(j < i){
					try{
						NodeFibHeap hashtag = remove();
						output.append(hashtag.key+",");
					}catch(Exception e){
						output.append(e.getMessage()+";");
					}
					j++;
				}
				printw.println(output.substring(0,output.length()-1));
				addBackRemovedNodes();
			}
			
		}
		
	}
	
	public static void main(String[] ar){
		HashTagCounter obj = new HashTagCounter();
		
		try {
			if(ar.length == 0)
				throw new Exception("No input file provided");
			
			File input = new File(ar[0]);
			File output = new File("output_file.txt");
			
			if(!output.exists())
				output.createNewFile();

			Scanner sin = new Scanner(input);

			ArrayList<String> lines = new ArrayList<String>();
			while(sin.hasNextLine()){
				String line = sin.nextLine();
				if(!line.equalsIgnoreCase("stop"))
					lines.add(line);
			}
			sin.close();

			FileWriter filew = new FileWriter(output);
			PrintWriter printw = new PrintWriter(new BufferedWriter(filew));

			obj.traverse(lines, printw);
			printw.close();

		} catch (FileNotFoundException e) {
			System.out.println("File "+ar[0]+" is not found\n");
		} catch(IOException e){
			System.out.println("File output_file.txt cannot be created, please create it manually");
		} catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
}
