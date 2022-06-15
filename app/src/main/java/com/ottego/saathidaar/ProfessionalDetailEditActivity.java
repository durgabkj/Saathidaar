package com.ottego.saathidaar;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.ottego.saathidaar.databinding.ActivityProfessionalDetailEditBinding;

import java.util.ArrayList;

public class ProfessionalDetailEditActivity extends AppCompatActivity {
    ActivityProfessionalDetailEditBinding b;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityProfessionalDetailEditBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        context = ProfessionalDetailEditActivity.this;
        userAnnualIncome();
        userWorkAs();
        UserWorkingWith();
    }

    private void userAnnualIncome() {
        String[] income = getResources().getStringArray(R.array.Income);
        ArrayAdapter incomeAdapter = new ArrayAdapter(context, R.layout.dropdown_item, income);
        //Setting the ArrayAdapter data on the Spinner
        b.UserAnnualIncome.setAdapter(incomeAdapter);

    }

    private void userWorkAs() {
        ArrayList<String> ProfessionAreaList = new ArrayList<>();
        ProfessionAreaList.add("Select");
        ProfessionAreaList.add("Banking Professional");
        ProfessionAreaList.add("Chartered Accountant");
        ProfessionAreaList.add("Company Secretary");
        ProfessionAreaList.add("Finance Professional");
        ProfessionAreaList.add("M.Eng (Hons)");
        ProfessionAreaList.add("Engineering Diploma");
        ProfessionAreaList.add("Investment Professional");
        ProfessionAreaList.add("Accounting Professional");
        ProfessionAreaList.add("Admin Professional");
        ProfessionAreaList.add("Actor");
        ProfessionAreaList.add("Advertising Professional");
        ProfessionAreaList.add("Entertainment Professional");
        ProfessionAreaList.add("Event Manager");
        ProfessionAreaList.add("Journalist");
        ProfessionAreaList.add("Media Professional");
        ProfessionAreaList.add("Public Relations Professional");
        ProfessionAreaList.add("Farming");
        ProfessionAreaList.add("Horticulturist");
        ProfessionAreaList.add("Agricultural Professional (Others)");
        ProfessionAreaList.add("Air Hostess / Flight Attendant");
        ProfessionAreaList.add("Pilot / Co-Pilot");
        ProfessionAreaList.add("Other Airline Professional");
        ProfessionAreaList.add("Architect");
        ProfessionAreaList.add("Interior Designer");
        ProfessionAreaList.add("Landscape Architect");
        ProfessionAreaList.add("Animator");
        ProfessionAreaList.add("Commercial Artist");
        ProfessionAreaList.add("Web / UX Designers");
        ProfessionAreaList.add("Artist (Others)");
        ProfessionAreaList.add("Beautician");
        ProfessionAreaList.add("Fashion Designer");
        ProfessionAreaList.add("Hairstylist");
        ProfessionAreaList.add("Jewellery Designer");
        ProfessionAreaList.add("Designer (Others)");
        ProfessionAreaList.add("Customer Support / BPO / KPO Professional");
        ProfessionAreaList.add("IAS / IRS / IES / IFS");
        ProfessionAreaList.add("Indian Police Services (IPS)");
        ProfessionAreaList.add("BCom (Hons)");
        ProfessionAreaList.add("PGD Finance");
        ProfessionAreaList.add("BCA");
        ProfessionAreaList.add("B.IT");
        ProfessionAreaList.add("BCS");
        ProfessionAreaList.add("BA Computer Science");
        ProfessionAreaList.add("Law Enforcement Employee (Others)");
        ProfessionAreaList.add("Airforce");
        ProfessionAreaList.add("Army");
        ProfessionAreaList.add("Navy");
        ProfessionAreaList.add("Defense Services (Others)");
        ProfessionAreaList.add("Lecturer");
        ProfessionAreaList.add("Professor");
        ProfessionAreaList.add("Research Assistant");
        ProfessionAreaList.add("Research Scholar");
        ProfessionAreaList.add("Teacher");
        ProfessionAreaList.add("Training Professional (Others)");
        ProfessionAreaList.add("Civil Engineer");
        ProfessionAreaList.add("Electronics / Telecom Engineer");
        ProfessionAreaList.add("Mechanical / Production Engineer");
        ProfessionAreaList.add("Non IT Engineer (Others)");
        ProfessionAreaList.add("Chef / Sommelier / Food Critic");
        ProfessionAreaList.add("Catering Professional");
        ProfessionAreaList.add("Hotel");
        ProfessionAreaList.add("Software Developer / Programmer");
        ProfessionAreaList.add("Software Consultant");
        ProfessionAreaList.add("Hardware");
        ProfessionAreaList.add("Software Professional (Others)");
        ProfessionAreaList.add("Lawyer");
        ProfessionAreaList.add("Legal Professional (Others)");
        ProfessionAreaList.add("Legal Assistant");
        ProfessionAreaList.add("Dentist");
        ProfessionAreaList.add("Doctor");
        ProfessionAreaList.add("Medical Transcriptionist");
        ProfessionAreaList.add("Nurse");
        ProfessionAreaList.add("Pharmacist");
        ProfessionAreaList.add("Physician Assistant");
        ProfessionAreaList.add("Physiotherapist / Occupational Therapist");
        ProfessionAreaList.add("Psychologist");
        ProfessionAreaList.add("Surgeon");
        ProfessionAreaList.add("Veterinary Doctor");
        ProfessionAreaList.add("Therapist (Others)");
        ProfessionAreaList.add("Medical / Healthcare Professional (Others)T");
        ProfessionAreaList.add("Merchant Naval Officer");
        ProfessionAreaList.add("Mariner");
        ProfessionAreaList.add("Marketing Professional");
        ProfessionAreaList.add("Sales Professional");
        ProfessionAreaList.add("Biologist / Botanist");
        ProfessionAreaList.add("Physicist");
        ProfessionAreaList.add("Science Professional (Others)");
        ProfessionAreaList.add("CxO / Chairman / Director / President");
        ProfessionAreaList.add("VP / AVP / GM / DGM");
        ProfessionAreaList.add("Sr. Manager / Manager");
        ProfessionAreaList.add("Consultant / Supervisor / Team Leads");
        ProfessionAreaList.add("Team Member / Staff");
        ProfessionAreaList.add("Agent / Broker / Trader / Contractor");
        ProfessionAreaList.add("Business Owner / Entrepreneur");
        ProfessionAreaList.add("Politician");
        ProfessionAreaList.add("Social Worker / Volunteer / NGO");
        ProfessionAreaList.add("Sportsman");
        ProfessionAreaList.add("Travel");
        ProfessionAreaList.add("Writer");
        ProfessionAreaList.add("Student");
        ProfessionAreaList.add("Retired");
        ProfessionAreaList.add("Not working");

        ArrayAdapter workingAsAdapter = new ArrayAdapter(context, R.layout.dropdown_item, ProfessionAreaList);
        //Setting the ArrayAdapter data on the Spinner
        b.UserWorkingAs.setAdapter(workingAsAdapter);
    }

    private void UserWorkingWith() {
        ArrayList<String> workingWithItemsList = new ArrayList<>();
        workingWithItemsList.add("Select");
        workingWithItemsList.add("Private Company");
        workingWithItemsList.add("Government / Public Sector");
        workingWithItemsList.add("Defense / Civil Services");
        workingWithItemsList.add("Business / Self Employed");
        workingWithItemsList.add("Not Working");
        workingWithItemsList.add("Engineering Diploma");

        ArrayAdapter workingWithAdapter = new ArrayAdapter(context, R.layout.dropdown_item, workingWithItemsList);
        //Setting the ArrayAdapter data on the Spinner
        b.UserWorkingWith.setAdapter(workingWithAdapter);
    }
}