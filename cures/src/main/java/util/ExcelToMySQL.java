package util;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.Properties;
import java.io.FileReader;
import model.Employee;

public class ExcelToMySQL {

	public static void main(String[] args) {
		String excelFilePath = "C:\\UserDemo.xlsx";
		FileReader file_reader = new FileReader(
				"/home/etheriumtechnologies/Production/installers/gitJava/All-Cures/cures/src/main/resources/whatsapi.properties");

		Properties p = new Properties();
		p.load(file_reader);
		String user = p.getProperty("DB_USER");
		String pass = p.getProperty("DB_PASS");
		String db = p.getProperty("DB_DBNAME");
		try {

			Connection con = null;
			Class.forName("com.mysql.jdbc.Driver");
//			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "allcures1", "root", "root@123");
  con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + db, user, pass);
			// Load Excel file
			FileInputStream excelFile = new FileInputStream(new File(excelFilePath));
			Workbook workbook = new XSSFWorkbook(excelFile);
			Sheet sheet = workbook.getSheetAt(0);

			// Set to store unique degrees
			Set<String> uniqueDegrees = new HashSet<>();

			// Iterate through each row
			for (Row row1 : sheet) {

				if (row1.getRowNum() == 0) {
					continue;
				}

				Cell degreeCell = row1.getCell(10);
				uniqueDegrees.add(degreeCell.getStringCellValue());
			}

			// Insert unique degrees into docdegrees table
			for (String degree : uniqueDegrees) {
				int degreeId = insertDegree(con, degree);
				System.out.println("Degree ID for " + degree + ": " + degreeId);
			}

			// Iterate through rows in Excel sheet
			for (Row row : sheet) {
				// Skip header row
				if (row.getRowNum() == 0) {
					continue;
				}

				// Create a new Employee object (assuming you have a corresponding Hibernate
				// entity)
				// Employee employee = new Employee();
				// employee.setEmployeeId((int) row.getCell(0).getNumericCellValue());
				// employee.setEmployeeName(row.getCell(1).getStringCellValue());
				// employee.setSalary(row.getCell(2).getNumericCellValue());

				// Save the Employee object to the database
				// session.save(employee);

				String NatlRegNo = row.getCell(1).getStringCellValue();
				System.out.println("NatlRegNo" + NatlRegNo);

				String NatlRegDate = row.getCell(14).getStringCellValue();
				System.out.println("NatlRegDate" + NatlRegDate);
				String fullName = row.getCell(2).getStringCellValue();

				// Split full name into first name, middle name, and last name
				String[] names = fullName.split("\\s+");
				String firstName = "";
				String middleName = "";
				String lastName = "";

				if (names.length >= 1) {
					firstName = names[0];
				}
				if (names.length >= 2) {
					lastName = names[names.length - 1];
				}
				if (names.length > 2) {
					middleName = String.join(" ", Arrays.copyOfRange(names, 1, names.length - 1));
				}
				System.out.println("First name" + firstName);
				System.out.println("middleName" + middleName);
				System.out.println("lastName" + lastName);

				String gender = row.getCell(3).getStringCellValue();
				int genderId = getGenderId(gender);
				System.out.println("genderId" + genderId);

				int contact = (int) row.getCell(4).getNumericCellValue();
				System.out.println("contact" + contact);

				String email = row.getCell(5).getStringCellValue();
				System.out.println("email" + email);

				String image = row.getCell(6).getStringCellValue();
				System.out.println("image" + image);

				int yearofGrad = (int) row.getCell(12).getNumericCellValue();
				System.out.println("yearofGrad" + yearofGrad);

				String state = row.getCell(17).getStringCellValue();
				int stateID = getRegStateId(con, state);

				if (stateID == -1) {
					stateID = insertRegState(con, state);
				}

				String city = row.getCell(18).getStringCellValue();
				int cityID = getCityId(con, city);

				if (cityID == -1) {
					cityID = insertCity(con, city);
				}

				String Address1 = row.getCell(8).getStringCellValue();
				// Get degree ID
				// String degree = row.getCell(10).getStringCellValue();

				// int degreeId = getDegreeId(con, degree);
				// System.out.println("degreeId"+degreeId);

				String RegStateBoard = row.getCell(20).getStringCellValue();
				int RegStateBoardID = getRegStateId(con, RegStateBoard);

				if (RegStateBoardID == -1) {
					RegStateBoardID = insertRegState(con, RegStateBoard);
				}

				String degree = row.getCell(10).getStringCellValue();
				int degreeId = getDegreeId(con, degree);

				if (degreeId == -1) {
					degreeId = insertDegree(con, degree);
				}
				System.out.println("degreeId" + degreeId);

				String med = row.getCell(11).getStringCellValue();
				int medId = getMedicineId(con, med);

				System.out.println("medId" + medId);
				
				int speciality=getSpecialtyId(con,med);
				// Insert doctor data
				int doctorId = insertDoctor(con, firstName, middleName, lastName, genderId, contact, email, image,
						RegStateBoardID,  NatlRegNo,NatlRegDate, medId,speciality);

				// Insert degree details into docdegrees table
				insertDocDegrees(con, doctorId, degreeId, yearofGrad);

				// Insert address details into docdegrees table
				insertDocAddress(con, doctorId, Address1, cityID, stateID);

				// transaction.commit();
				System.out.println("Data inserted successfully!");

			}
		} catch (Exception e) {
			// if (transaction != null) {
			// transaction.rollback();
			// }
			e.printStackTrace();
		}
	}

	private static int getGenderId(String gender) {
		if ("MALE".equalsIgnoreCase(gender) || "M".equalsIgnoreCase(gender) || "Male".equalsIgnoreCase(gender)
				|| "male".equalsIgnoreCase(gender)) {
			return 1; // Male
		} else if ("FEMALE".equalsIgnoreCase(gender) || "F".equalsIgnoreCase(gender)
				|| "Female".equalsIgnoreCase(gender) || "female".equalsIgnoreCase(gender)) {
			return 2; // Female
		} else {
			return 0; // Default or unknown
		}
	}

	private static int getDegreeId(Connection connection, String degreeName) throws Exception {
		String query = "SELECT DegID FROM masterdocdegrees WHERE DegDesc = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, degreeName);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getInt("DegID");
			} else {
				return -1;
			}
		}
	}

	private static int getMedicineId(Connection connection, String Name) throws Exception {
		String query = "SELECT id FROM medicinetype WHERE name = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, Name);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getInt("id");
			} else {
				return -1;
			}
		}
	}

	private static int getSpecialtyId(Connection connection, String Name) throws Exception {
		String query = "SELECT splid FROM specialties WHERE spl_name = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, Name);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getInt("splid");
			} else {
				return -1;
			}
		}
	}
		
	private static int insertRegState(Connection connection, String stateName) throws Exception {
		String query = "INSERT INTO states (statename) VALUES (?)";
		try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, stateName);
			statement.executeUpdate();
			ResultSet resultSet = statement.getGeneratedKeys();
			if (resultSet.next()) {
				return resultSet.getInt(1);
			} else {
				throw new Exception("Failed to insert state");
			}
		}
	}

	private static int getRegStateId(Connection connection, String stateName) throws Exception {
		String query = "SELECT codeid FROM states WHERE statename = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, stateName);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getInt("codeid");
			} else {
				return -1;
			}
		}
	}

	private static int insertCity(Connection connection, String cityName) throws Exception {
		String query = "INSERT INTO city (cityname) VALUES (?)";
		try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, cityName);
			statement.executeUpdate();
			ResultSet resultSet = statement.getGeneratedKeys();
			if (resultSet.next()) {
				return resultSet.getInt(1);
			} else {
				throw new Exception("Failed to insert state");
			}
		}
	}

	private static int getCityId(Connection connection, String cityName) throws Exception {
		String query = "SELECT citycode FROM city WHERE cityname = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, cityName);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getInt("citycode");
			} else {
				return -1;
			}
		}
	}

	private static int insertDegree(Connection connection, String degreeName) throws Exception {
		String query = "INSERT INTO masterdocdegrees (DegDesc) VALUES (?)";
		try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, degreeName);
			statement.executeUpdate();
			ResultSet resultSet = statement.getGeneratedKeys();
			if (resultSet.next()) {
				return resultSet.getInt(1);
			} else {
				throw new Exception("Failed to insert degree");
			}
		}
	}

	private static int insertDoctor(Connection connection, String firstName, String middleName, String lastName,
			int genderID, int contact, String email, String img_Loc, int RegWithStateBoardID, String NatlRegNo,
			String Natl_Reg_Date, int MedType, int speciality) throws Exception {
		String query = "INSERT INTO Doctors_New (gender, telephone_nos, prefix, docname_first, docname_middle, docname_last, email, img_Loc, Natl_Reg_Date, RegWithStateBoardID, NatlRegNo, MedicineTypeID, primary_spl) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?)";
		try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			System.out.println("NatlRegNo"+NatlRegNo);
			System.out.println("Natl_Reg_Date"+Natl_Reg_Date);
			statement.setInt(1, genderID);
			statement.setInt(2, contact);
			statement.setString(3, "Dr.");
			statement.setString(4, firstName);
			statement.setString(5, middleName);
			statement.setString(6, lastName);
			statement.setString(7, email);
			statement.setString(8, img_Loc);
			statement.setString(9, convertToMySQLDate(Natl_Reg_Date));
			statement.setInt(10, RegWithStateBoardID);
			statement.setString(11, NatlRegNo);
			statement.setInt(12, MedType);
			statement.setInt(13, speciality);
			statement.executeUpdate();

			System.out.println(statement);
			ResultSet resultSet = statement.getGeneratedKeys();
			if (resultSet.next()) {
				return resultSet.getInt(1);
			} else {
				throw new Exception("Failed to insert doctor");
			}
		}
	}

	private static String convertToMySQLDate(String inputDate) throws ParseException {
	    DateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy");
	    DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");

	    // Parse the input date using java.util.Date
	    java.util.Date utilDate = originalFormat.parse(inputDate);

	    // Convert to java.sql.Date
	    java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

	    // Format the java.sql.Date as a string in the target format
	    return targetFormat.format(sqlDate);
	}

	private static void insertDocDegrees(Connection connection, int doctorId, int degreeId, int yearofGrad)
			throws Exception {
		String query = "INSERT INTO DoctorDegrees (DocID, DegreeID,YearOfGrad) VALUES (?, ?,?)";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, doctorId);
			statement.setInt(2, degreeId);
			statement.setInt(3, yearofGrad);
			statement.executeUpdate();
		}
	}

	private static void insertDocAddress(Connection connection, int doctorId, String Address1, int City, int State)
			throws Exception {
		String query = "INSERT INTO DoctorAddresses (DocID,AddressTypeID,Address1,City,State,Country) VALUES (?, ?,?,?,?,?)";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, doctorId);
			statement.setInt(2, 1);
			statement.setString(3, Address1);
			statement.setInt(4, City);
			statement.setInt(5, State);
			statement.setInt(6, 9);

			statement.executeUpdate();
		}
	}

}
