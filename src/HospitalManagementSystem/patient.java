package HospitalManagementSystem;
import java.sql.*;
import java.util.Scanner;

public class patient {
    private Connection connection;

    private Scanner scanner = new Scanner(System.in);

    public patient(Connection connection, Scanner scanner){
        this.connection= connection;
        this.scanner=scanner;

    }
    public void addpatient(){
        System.out.println("Enter Your Name :");
        scanner.nextLine();
        String name = scanner.nextLine();

        System.out.println("Enter Patient Age :");
        int age = scanner.nextInt();
        System.out.println("Enter patient gender:");
        scanner.nextLine();
        String gender = scanner.next();

        try{
            String query = "INSERT INTO PATIENT(NAME, AGE, GENDER)VALUES(?,?,?)";
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,name);
            preparedStatement.setInt(2,age);
            preparedStatement.setString(3,gender);
            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows>0){
                System.out.println("Patient Data added successfully!!");
            }else{
                System.out.println("Failed to add Patient");
            }



        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void viewpatient(){
        String query = "select * from patient";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("patients: ");
            System.out.println("+------------+--------------------+-------------+------------+");
            System.out.println("| Patient Id | Name               | Age         | Gender        |");
            System.out.println("+------------+--------------------+-------------+------------+");

            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                System.out.printf("|%-12s|%-20s|%-10s|%-12s|\n",id,name,age,gender);
                System.out.println("+------------+--------------------+-------------+------------+");

            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public boolean getPatientById(int id) {
        String query = "SELECT * FROM patient WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
