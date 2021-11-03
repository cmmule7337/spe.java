package ques1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;

public class speci
{
	static Connection conn;
	static Statement stmt;
	static PreparedStatement pstmt;
	static CallableStatement cstmt;
	static ResultSet rs;
	static BufferedReader br;
	
	public static void main(String args[]) throws IOException
	{
		while (true)
		{
			switch(showMenu())
			{
				case 1: addNewSpecialization();
						break;
				case 2: registerStudent();
						break;
				case 3: showAvailableSlots();
						break;
				case 4:	showStudentRecords();
						break;
				case 5: System.exit(0);
						break;
				default:System.out.println("\nInvalid Choice");
			}
		}
	}
	
	public static int showMenu() throws IOException
	{
		System.out.println("\n+---------------------------------------------------------+");
		System.out.println("|     K L UNIVERSITY STUDENT SPECIALIZATION SELECTION     |");
		System.out.println("+----+----------------------------------------------------+");
		System.out.println(String.format("| 1. | %-50s |", "Add a New Specialization"));
		System.out.println(String.format("| 2. | %-50s |", "Register a Student to a Specialization"));
		System.out.println(String.format("| 3. | %-50s |", "Show the Available Slots of all Specializations"));
		System.out.println(String.format("| 4. | %-50s |", "Show All Students Records"));
		System.out.println(String.format("| 5. | %-50s |", "Exit"));
		System.out.println("+----+----------------------------------------------------+");
		
		System.out.print("\nEnter Your Choice : ");
		br = new BufferedReader(new InputStreamReader(System.in));
		
		int choice = Integer.parseInt(br.readLine());
		return choice;
	}
	
	public static void addNewSpecialization() throws IOException
	{
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jfsd", "root", "cmmule7337@");
			br = new BufferedReader(new InputStreamReader(System.in));
			
			String query = "insert into specialization values(?, ?, ?, ?);";
			PreparedStatement pstmt = conn.prepareStatement(query);
			
			System.out.print("\nEnter Specialization ID : ");
			int spec_id = Integer.parseInt(br.readLine());
			System.out.print("Enter Specialization Name : ");
			String spec_name = br.readLine();
			System.out.print("Enter Total Slots : ");
			int total_slots = Integer.parseInt(br.readLine());
			System.out.print("Enter Available Slots : ");
			int available_slots = Integer.parseInt(br.readLine());
			
			pstmt.setInt(1, spec_id);
			pstmt.setString(2, spec_name);
			pstmt.setInt(3, total_slots);
			pstmt.setInt(4, available_slots);
			pstmt.execute();
			
			System.out.println("\nSpecialization Added Successfully");
			pstmt.close();
			conn.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void registerStudent() throws IOException
	{
		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));	
			int specid;
			while (true)
			{
				showSpecializations();
				System.out.print("\nEnter Preferred Specilization ID : ");
				specid = Integer.parseInt(br.readLine());
				if (checkSlots(specid))
				{
					break;
				}
				else
				{
					System.out.println("\nNo slots available for this specialization.\nPlease select another specialization.");
				}
			}
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jfsd", "root", "0000");
			
			System.out.print("\nEnter Student ID : ");
			int id = Integer.parseInt(br.readLine());
			System.out.print("Enter Student Name : ");
			String name = br.readLine();
			System.out.print("Enter Student Gender (Male, Female, Other) : ");
			String gender = br.readLine();
			System.out.print("Enter Student Year of Study (1, 2, 3, 4) : ");
			int yos = Integer.parseInt(br.readLine());
			System.out.print("Enter Student Department (CSE, ECE, ECS, ME, CE, BT) : ");
			String dept = br.readLine();
			System.out.print("Enter Student Email : ");
			String email = br.readLine();
			System.out.print("Enter Student CGPA : ");
			double cgpa = Double.parseDouble(br.readLine());
			System.out.print("Enter Student Backlogs : ");
			int backlogs = Integer.parseInt(br.readLine());
			
			pstmt = conn.prepareStatement("update specialization set available_slots = available_slots - 1 where specid = ?");
			pstmt.setInt(1, specid);
			pstmt.execute();
			
			pstmt = conn.prepareStatement("insert into students values(?, ?, ?, ?, ?, ?, ?, ?, ?);");
			pstmt.setInt(1, id);
			pstmt.setString(2, name);
			pstmt.setString(3, gender);
			pstmt.setInt(4, yos);
			pstmt.setString(5, dept);
			pstmt.setString(6, email);
			pstmt.setDouble(7, cgpa);
			pstmt.setInt(8, backlogs);
			pstmt.setInt(9, specid);
			pstmt.execute();
			
			System.out.println("\nStudent Registered to Specialization Successfully");
			pstmt.close();
			conn.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void showSpecializations()
	{
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jfsd", "root", "0000");
			
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select specid, specname from specialization");
			
			System.out.println("\n+------------------------------------------------------------------------+");
			System.out.println("|                         LIST OF SPECIALIZATIONS                        |");
			System.out.println("+-------------------+----------------------------------------------------+");
			System.out.println("| Specialization ID |                 Specialization Name                |");
			System.out.println("+-------------------+----------------------------------------------------+");
			while (rs.next())
			{
				System.out.println(String.format("| %17d | %-50s |", rs.getInt(1), rs.getString(2)));
			}
			System.out.println("+-------------------+----------------------------------------------------+");
			
			rs.close();
			stmt.close();
			conn.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static boolean checkSlots(int id)
	{
		boolean check = false;
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jfsd", "root", "0000");
			
			cstmt = conn.prepareCall("{call checkAvailableSlots(?, ?)}");
			cstmt.setInt(1, id);
			cstmt.registerOutParameter(2, Types.VARCHAR);
			cstmt.execute();
			
			int available = cstmt.getInt(2);
			cstmt.close();
			conn.close();
			if (available == 0)
				check = false;
			else
				check = true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return check;
	}
	
	public static void showAvailableSlots()
	{
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jfsd", "root", "0000");
			
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from specialization");
			
			System.out.println("\n+--------------------------------------------------------------------------------------------------------+");
			System.out.println("|                                             SPECIALIZATIONS                                            |");
			System.out.println("+-------------------+----------------------------------------------------+-------------+-----------------+");
			System.out.println("| Specialization ID |                 Specialization Name                | Total Slots | Available Slots |");
			System.out.println("+-------------------+----------------------------------------------------+-------------+-----------------+");
			while (rs.next())
			{
				System.out.println(String.format("| %17d | %-50s | %11d | %15d |", rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4)));
			}
			System.out.println("+-------------------+----------------------------------------------------+-------------+-----------------+");
			
			rs.close();
			stmt.close();
			conn.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void showStudentRecords()
	{
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/skill1", "root", "cmmule7337@");
			
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from students st, specialization sp where st.student_specid = sp.specid");
			
			System.out.println("\n+------------------------------------------------------------------------------------------------------------------------------------------------------------------------+");
			System.out.println("|                                                                             STUDENTS RECORDS                                                                           |");
			System.out.println("+------------+--------------------------------+--------+---------------+------------+---------------------------------+------+----------+--------------------------------+");
			System.out.println("|     ID     |              Name              | Gender | Year of Study | Department |              Email              | CGPA | Backlogs |         Specialization         |");
			System.out.println("+------------+--------------------------------+--------+---------------+------------+---------------------------------+------+----------+--------------------------------+");
			while (rs.next())
			{
				System.out.println(String.format("| %10d | %-30s | %-6s | %13d | %-10s | %-31s | %4.2f | %8d | %-30s |", rs.getInt(1), rs.getString(2), 
						rs.getString(3), rs.getInt(4), rs.getString(5), rs.getString(6), rs.getDouble(7), rs.getInt(8), rs.getString(11)));
			}
			System.out.println("+------------+--------------------------------+--------+---------------+------------+---------------------------------+------+----------+--------------------------------+");
			
			rs.close();
			stmt.close();
			conn.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}