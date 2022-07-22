package com.operatordb;

public class App 
{
    public static void main( String[] args )
    {
        Database.insertUser("05320000000", "Mehmet", "Test", 100, "Turcell",1);
        Database.insertUser("05320000001", "Ali", "Test", 200, "Vodafone",1);
        Database.insertUser("05320000002", "Veli", "Test", 300, "Turk Telekom",1);
        //Database.doesUserExists("05320000003");
    }
}
