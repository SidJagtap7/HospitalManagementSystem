package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "mysql";



    static void bookAppointment(patient patient, Doctor doctor, Connection connection, Scanner scanner){
        System.out.println("Enter Patient Id: ");
        int patientId = scanner.nextInt();
        System.out.println("Enter Doctor Id:");
        int doctorId = scanner.nextInt();
        System.out.println("Enter appointment date (YYYY-MM-DD):");
        String appointmentDate = scanner.next();
        if(patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)){
            if (checkdoctorAvailability(doctorId, appointmentDate,connection)){
                String appointmentQuery= "INSERT INTO appointments(patient_id, doctor_id,appointment_date) VALUES(?, ?, ?)";
                try {
                    PreparedStatement preparedStatement =connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1,patientId);
                    preparedStatement.setInt(2,doctorId);
                    preparedStatement.setString(3,appointmentDate);
                    int rowsaffected = preparedStatement.executeUpdate();
                    if(rowsaffected>0){
                        System.out.println("Appointment Booked!");
                    }else{
                        System.out.println("Fail to Book Appointment! ");
                    }

                }catch(SQLException e){
                    e.printStackTrace();
                }
            }else {
                System.out.println("Doctor is not available on the selected Date.");
            }


        }else{
            System.out.println("Either doctor or patient dosen't exits!!!");
        }
    }

    static boolean checkdoctorAvailability(int doctorId, String appointmentDate, Connection connection){
        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_Id = ? AND appointment_Date = ? ";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setString(2, appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int count = resultSet.getInt(1);
                return count == 0;
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        try{
            Connection connection = DriverManager.getConnection(url, username, password);
            patient patient =new patient(connection, scanner);
            Doctor doctor = new Doctor(connection);
            while(true){
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patient");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.println("Enter your choice :");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1 -> {
                        //add patient
                        patient.addpatient();
                        System.out.println();
                    }
                    case 2 -> {
                        //view patient
                        patient.viewpatient();
                        System.out.println();
                    }
                    case 3 -> {
                        //view doctors
                        doctor.viewDoctor();
                        System.out.println();
                    }
                    case 4 -> {
                        //Book appointment
                        bookAppointment(patient, doctor, connection, scanner);
                        System.out.println();
                    }
                    case 5 -> {
                        //Exit
                        System.out.println("Thank you for using Hospital Management System!!");
                        return;
                    }
                    default -> System.out.println("Enter Valid Choice");
                }

            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
