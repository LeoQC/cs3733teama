package entities;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;

public class JavaDatabaseStaffInfoTest {

    JavaDatabaseStaffInfo db;
    private Connection conn = null;
    private Statement stmt = null;

    @Before
    public void connectToDB() {
        testDatabaseConnection();
        // Create the database testdb with the table TEST_STAFF
        db = new JavaDatabaseStaffInfo("jdbc:derby://localhost:1527/testdb;create=true", "TEST_STAFF");
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            // Get a connection
            stmt = conn.createStatement();
            stmt.execute("INSERT INTO TEST_STAFF VALUES('staffOne', 'SECURITY')");
            stmt.execute("INSERT INTO TEST_STAFF VALUES('staffTwo', 'TRANSPORT')");
            stmt.execute("INSERT INTO TEST_STAFF VALUES('staffThree', 'INTERPRETER')");
            stmt.execute("INSERT INTO TEST_STAFF VALUES('staffFour', 'TRANSPORT')");
            // Add languages into TEST_STAFF_LANGUAGES db
            stmt.execute("INSERT INTO TEST_STAFF_LANGUAGES VALUES('staffOne', 'English')");
            stmt.execute("INSERT INTO TEST_STAFF_LANGUAGES VALUES('staffOne', 'German')");
            stmt.execute("INSERT INTO TEST_STAFF_LANGUAGES VALUES('staffTwo', 'English')");
            stmt.execute("INSERT INTO TEST_STAFF_LANGUAGES VALUES('staffTwo', 'French')");
            stmt.execute("INSERT INTO TEST_STAFF_LANGUAGES VALUES('staffThree', 'English')");
            stmt.execute("INSERT INTO TEST_STAFF_LANGUAGES VALUES('staffThree', 'Russian')");
            stmt.close();
        }
        catch (Exception except) {
            except.printStackTrace();
            //assertEquals(false, true); // fail the test if there is an exception
        }
    }

    @Test
    public void testDatabaseConnection() {
        // Drop the tables from before
        try {
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/testdb;create=true");
            stmt = conn.createStatement();
            stmt.execute("DROP TABLE APP.TEST_STAFF");
            stmt.execute("DROP TABLE APP.TEST_STAFF_LANGUAGES");
            stmt.close();
        } catch (Exception except) {
            //except.printStackTrace();
        }
    }

    @Test
    public void findQualified() throws Exception {
        // Find a security guard that speaks any language
        StaffAttrib attrib = new StaffAttrib(StaffType.SECURITY, new HashSet<>());
        StaffType type = db.findQualified(attrib).getStaffType();
        assertEquals(StaffType.SECURITY, type);

        // Find an interpreter that speaks any language
        attrib = new StaffAttrib(StaffType.INTERPRETER, new HashSet<>());
        type = db.findQualified(attrib).getStaffType();
        assertEquals(StaffType.INTERPRETER, type);

        // Find an interpreter that speaks Russian and English
        Set<Language> lan = new HashSet<>();
        lan.add(Language.Russian);
        lan.add(Language.English);
        attrib = new StaffAttrib(StaffType.INTERPRETER, lan);
        ServiceStaff found = db.findQualified(attrib);
        assertEquals(StaffType.INTERPRETER, found.getStaffType());
        assertEquals(lan, found.getLanguages());

        // Find an interpreter that speaks Russian and German
        // But one doesn't exist, so it returns null
        lan = new HashSet<>();
        lan.add(Language.Russian);
        lan.add(Language.German);
        attrib = new StaffAttrib(StaffType.INTERPRETER, lan);
        assertEquals(null, db.findQualified(attrib));

        // Find an Transport that speaks English
        lan = new HashSet<>();
        lan.add(Language.English);
        attrib = new StaffAttrib(StaffType.TRANSPORT, lan);
        found = db.findQualified(attrib);
        assertEquals(StaffType.TRANSPORT, found.getStaffType());
        assertEquals(true, found.getLanguages().containsAll(lan));

        // Find staffThree and see if he speaks all the languages defined above
        // English and Russian (staffThree is an interpreter)
        attrib = new StaffAttrib(StaffType.INTERPRETER, null);
        found = db.findQualified(attrib);
        assertEquals(StaffType.INTERPRETER, found.getStaffType());
        Set<Language> exp = new HashSet<>();
        exp.add(Language.English);
        exp.add(Language.Russian);
        assertEquals(exp, found.getLanguages());
    }

}