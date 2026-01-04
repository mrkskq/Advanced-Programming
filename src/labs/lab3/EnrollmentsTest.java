package labs.lab3;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

// TODO: Add classes and implement methods
class Applicant{
    private int id;
    private String name;
    private double gpa;
    private List<SubjectWithGrade> subjectWithGrade;
    private StudyProgramme studyProgramme;

    public Applicant(int id, String name, double gpa, List<SubjectWithGrade> subjectWithGrade, StudyProgramme studyProgramme) {
        this.id = id;
        this.name = name;
        this.gpa = gpa;
        this.subjectWithGrade = subjectWithGrade;
        this.studyProgramme = studyProgramme;
    }

    public void addSubjectAndGrade(String subject, int grade){
        subjectWithGrade.add(new SubjectWithGrade(subject, grade));
    }

    public double calculatePoints(){
        double points = gpa * 12;
        return points * 1.2;
    }

    public static final Comparator<Applicant> byPoints = Comparator.comparing(Applicant::calculatePoints);

    @Override
    public String toString() {
        return String.format("Id: %s, Name: %s, GPA: %.1f - %f\n", id, name, gpa, calculatePoints());
    }
}

class StudyProgramme{
    private String code;
    private String name;
    private int numPublicQuota;
    private int numPrivateQuota;
    private int enrolledInPublicQuota;
    private int enrolledInPrivateQuota;
    private List<Applicant> applicants;
    private Faculty faculty;

    public StudyProgramme(String code, String name, Faculty faculty, int pubQ, int privQ) {
        this.code = code;
        this.name = name;
        this.faculty = faculty;
        this.numPublicQuota = pubQ;
        this.numPrivateQuota = privQ;
        this.applicants = new ArrayList<>();
    }

    public void setEnrolledInPublicQuota(int enrolledInPublicQuota) {
        this.enrolledInPublicQuota = enrolledInPublicQuota;
    }

    public void setEnrolledInPrivateQuota(int enrolledInPrivateQuota) {
        this.enrolledInPrivateQuota = enrolledInPrivateQuota;
    }

    public void calculateEnrollmentNumbers(){

    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public void addApplicant(Applicant applicant){
        applicants.add(applicant);
    }

    public static final Comparator<StudyProgramme> byName = Comparator.comparing(StudyProgramme::getName);

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: " + name + "\n");
        sb.append("Public Quota:\n");
        for (Applicant a : applicants) {
            sb.append(a.toString());
        }
        sb.append("Private Quota:\n");
        sb.append("Rejected:\n\n");
        return sb.toString();
    }
}

class Faculty{
    private String shortName;
    private List<String> appropriateSubjects;
    private List<StudyProgramme> studyProgrammes;

    public Faculty(String shortName) {
        this.shortName = shortName;
        this.appropriateSubjects = new ArrayList<>();
        this.studyProgrammes = new ArrayList<>();
    }

    public void addSubject(String s){
        appropriateSubjects.add(s);
    }

    public void addStudyProgramme(StudyProgramme sp){
        studyProgrammes.add(sp);
    }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Faculty: " + shortName + "\n");
        sb.append("Subjects: [");
        sb.append(appropriateSubjects.stream().collect(Collectors.joining(", ")));
        sb.append("]\n");
        sb.append("Study Programmes:\n");
        for (StudyProgramme p : studyProgrammes.stream().sorted(StudyProgramme.byName).collect(Collectors.toList())) {
            sb.append(p.toString());
        }
        return sb.toString();
    }
}


class SubjectWithGrade
{
    private String subject;
    private int grade;
    public SubjectWithGrade(String subject, int grade) {
        this.subject = subject;
        this.grade = grade;
    }
    public String getSubject() {
        return subject;
    }
    public int getGrade() {
        return grade;
    }
}

class Enrollment{
    private Applicant applicant;
    private StudyProgramme studyProgramme;

    public Enrollment(Applicant applicant, StudyProgramme studyProgramme) {
        this.applicant = applicant;
        this.studyProgramme = studyProgramme;
    }
}

class EnrollmentsIO {
    public static void printRanked(List<Faculty> faculties) {
        for (Faculty f : faculties) {
            System.out.println(f.toString());
        }
    }

    public static List<Enrollment> readEnrollments(List<StudyProgramme> studyProgrammes, InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        List<Enrollment> enrollments = new ArrayList<>();

        while (true){
            line = reader.readLine();
            if (line == null || line.isEmpty()){
                break;
            }

            String []parts = line.split(";");
            String code = parts[parts.length-1];
            int id = Integer.parseInt(parts[0]);
            String name = parts[1];
            double gpa = Double.parseDouble(parts[2]);

            String subjects = String.join(",", Arrays.copyOfRange(parts, 3, parts.length));
            String []tokens = subjects.split(",");
            List<SubjectWithGrade> subjectsWithGrade = new ArrayList<>();
            for (int i = 0, j = 1; i < tokens.length-1 && j < tokens.length-1; i+=2, j+=2) {
                String subject = tokens[i];
                int grade = Integer.parseInt(tokens[j]);
                SubjectWithGrade s = new SubjectWithGrade(subject, grade);
                subjectsWithGrade.add(s);
            }
            StudyProgramme target = null;
            for (StudyProgramme sp : studyProgrammes) {
                if (sp.getCode().equalsIgnoreCase(code)) {
                    target = sp;
                    break;
                }
            }
            Applicant applicant = new Applicant(id, name, gpa, subjectsWithGrade, target);
            target.addApplicant(applicant);
            enrollments.add(new Enrollment(applicant, target));
        }
        return enrollments;
    }
}

public class EnrollmentsTest {

    public static void main(String[] args) throws IOException {
        Faculty finki = new Faculty("FINKI");
        finki.addSubject("Mother Tongue");
        finki.addSubject("Mathematics");
        finki.addSubject("Informatics");

        Faculty feit = new Faculty("FEIT");
        feit.addSubject("Mother Tongue");
        feit.addSubject("Mathematics");
        feit.addSubject("Physics");
        feit.addSubject("Electronics");

        Faculty medFak = new Faculty("MEDFAK");
        medFak.addSubject("Mother Tongue");
        medFak.addSubject("English");
        medFak.addSubject("Mathematics");
        medFak.addSubject("Biology");
        medFak.addSubject("Chemistry");

        StudyProgramme si = new StudyProgramme("SI", "Software Engineering", finki, 4, 4);
        StudyProgramme it = new StudyProgramme("IT", "Information Technology", finki, 2, 2);
        finki.addStudyProgramme(si);
        finki.addStudyProgramme(it);

        StudyProgramme kti = new StudyProgramme("KTI", "Computer Technologies and Engineering", feit, 3, 3);
        StudyProgramme ees = new StudyProgramme("EES", "Electro-energetic Systems", feit, 2, 2);
        feit.addStudyProgramme(kti);
        feit.addStudyProgramme(ees);

        StudyProgramme om = new StudyProgramme("OM", "General Medicine", medFak, 6, 6);
        StudyProgramme nurs = new StudyProgramme("NURS", "Nursing", medFak, 2, 2);
        medFak.addStudyProgramme(om);
        medFak.addStudyProgramme(nurs);

        List<StudyProgramme> allProgrammes = new ArrayList<>();
        allProgrammes.add(si);
        allProgrammes.add(it);
        allProgrammes.add(kti);
        allProgrammes.add(ees);
        allProgrammes.add(om);
        allProgrammes.add(nurs);

        EnrollmentsIO.readEnrollments(allProgrammes, System.in);

        List<Faculty> allFaculties = new ArrayList<>();
        allFaculties.add(finki);
        allFaculties.add(feit);
        allFaculties.add(medFak);

        allProgrammes.stream().forEach(StudyProgramme::calculateEnrollmentNumbers);

        EnrollmentsIO.printRanked(allFaculties);

    }


}
