package com.ottego.saathidaar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.ottego.multipleselectionspinner.MultipleSelection;
import com.ottego.saathidaar.databinding.FragmentPartnerPreferenceBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class PartnerPreferenceFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    MultipleSelection multi_SelectionCountry, multi_SelectionProfessionArea, multi_SelectionMotherTongue ,multi_SelectionQualification,multi_SelectionWorkingWith;

    TextView textView,tvMultipleMaritalStatus;
    boolean[] selectedLanguage;
    ArrayList<Integer> religionList = new ArrayList<>();
    String[] ReligionArray = {"Java", "C++", "Kotlin", "C", "Python", "Javascript"};

    //For MaritalStatus....
    boolean[] selectedMaritalStatus;
    ArrayList<Integer> MaritalStatusList = new ArrayList<>();
    String[] MaritalStatusArray = {"Never Married","Divorce", "Widowed", "Awaiting Divorce", "Annulled"};



    // Initialize variables
    Spinner spMin,spMax,spFromHeight,spToHeight;

    ArrayList<String> AgeList =new ArrayList<>();
    ArrayList<String> minList=new ArrayList<>();
    ArrayList<String> maxList=new ArrayList<>();
    ArrayAdapter<String> minAdapter,maxAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PartnerPreferenceFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static PartnerPreferenceFragment newInstance(String param1, String param2) {
        PartnerPreferenceFragment fragment = new PartnerPreferenceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_partner_preference, container, false);
        // assign variable
        multi_SelectionCountry = view.findViewById(R.id.multi_SelectionCountry);
       // multi_Selection = view.findViewById(R.id.multi_Selection);
        multi_SelectionMotherTongue=view.findViewById(R.id.multi_SelectionMotherTongue);
        multi_SelectionQualification=view.findViewById(R.id.multi_SelectionQualification);
        tvMultipleMaritalStatus=view.findViewById(R.id.tvMultipleMaritalStatus);
        multi_SelectionWorkingWith=view.findViewById(R.id.multi_SelectionWorkingWith);
        multi_SelectionProfessionArea=view.findViewById(R.id.multi_SelectionProfessionArea);
        spToHeight=view.findViewById(R.id.spToHeight);
        spFromHeight=view.findViewById(R.id.spFromHeight);
        spToHeight=view.findViewById(R.id.spToHeight);
        // assign variables
        spMin=view.findViewById(R.id.sp_min);
        spMax= view.findViewById(R.id.sp_max);

        multi_SelectionCountry.setItems(getItems());
        multi_SelectionCountry.setOnItemSelectedListener(new MultipleSelection.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, boolean isSelected, int position) {
//                Toast.makeText(MainActivity.this, "On Item selected : " + isSelected, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSelectionCleared() {
                Toast.makeText(getContext(), "All items are unselected", Toast.LENGTH_SHORT).show();
            }
        });
        multi_SelectionWorkingWith();
         multipleSelectionMotherTongue();
        multipleSelectionQualification();
        multipleMaritalStatusSelectionCheckBox();
       // multipleReligionSelectionCheckBox();
        multi_SelectionmultiProfessionArea();

        Height();
        SpinnerAge();
        return view;
    }

    private void Height() {
        String[] fromHeight = getResources().getStringArray(R.array.Height);
        ArrayAdapter aa = new ArrayAdapter(requireActivity(), R.layout.dropdown_item, fromHeight);
        //Setting the ArrayAdapter data on the Spinner
        spFromHeight.setAdapter(aa);
        spToHeight.setAdapter(aa);
    }

    private void multi_SelectionmultiProfessionArea() {
        multi_SelectionProfessionArea.setItems(getProfessionAreaItems());
        multi_SelectionProfessionArea.setOnItemSelectedListener(new MultipleSelection.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, boolean isSelected, int position) {
//                Toast.makeText(MainActivity.this, "On Item selected : " + isSelected, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSelectionCleared() {
//                Toast.makeText(MainActivity.this, "All items are unselected", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private List getProfessionAreaItems() {
        ArrayList<String> ProfessionAreaList = new ArrayList<>();
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
       return ProfessionAreaList;
    }

    private void multi_SelectionWorkingWith() {
        multi_SelectionWorkingWith.setItems(getWorkingWithItems());
        multi_SelectionWorkingWith.setOnItemSelectedListener(new MultipleSelection.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, boolean isSelected, int position) {
//                Toast.makeText(MainActivity.this, "On Item selected : " + isSelected, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSelectionCleared() {
//                Toast.makeText(MainActivity.this, "All items are unselected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List getWorkingWithItems() {
        ArrayList<String> workingWithItemsList = new ArrayList<>();
        workingWithItemsList.add("Private Company");
        workingWithItemsList.add("Government / Public Sector");
        workingWithItemsList.add("Defense / Civil Services");
        workingWithItemsList.add("Business / Self Employed");
        workingWithItemsList.add("Not Working");
        workingWithItemsList.add("Engineering Diploma");
        return workingWithItemsList;
    }

    private void multipleSelectionQualification() {
        multi_SelectionQualification.setItems(getQualificationItems());
        multi_SelectionQualification.setOnItemSelectedListener(new MultipleSelection.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, boolean isSelected, int position) {
//                Toast.makeText(MainActivity.this, "On Item selected : " + isSelected, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSelectionCleared() {
//                Toast.makeText(MainActivity.this, "All items are unselected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List getQualificationItems() {
        ArrayList<String> qualificationList = new ArrayList<>();
        qualificationList.add("B.E / B.Tech");
        qualificationList.add("M.E / M.Tech");
        qualificationList.add("M.S Engineering");
        qualificationList.add("B.Eng (Hons)");
        qualificationList.add("M.Eng (Hons)");
        qualificationList.add("Engineering Diploma");
        qualificationList.add("AE");
        qualificationList.add("AET");
        qualificationList.add("High school");
        qualificationList.add("B.A");
        qualificationList.add("B.Ed");
        qualificationList.add("BJMC");
        qualificationList.add("BFA");
        qualificationList.add("B.Arch");
        qualificationList.add("B.Des");
        qualificationList.add("BMM");
        qualificationList.add("MFA");
        qualificationList.add("M.Ed");
        qualificationList.add("M.A");
        qualificationList.add("MSW");
        qualificationList.add("MJMC");
        qualificationList.add("M.Arch");
        qualificationList.add("M.Des");
        qualificationList.add("BA (Hons)");
        qualificationList.add("DFA");
        qualificationList.add("D.Ed");
        qualificationList.add("D.Arch");
        qualificationList.add("AA");
        qualificationList.add("AFA");
        qualificationList.add("Less than high school");
        qualificationList.add("B.Com");
        qualificationList.add("CA / CPA");
        qualificationList.add("CFA");
        qualificationList.add("CS");
        qualificationList.add("BSc / BFin");
        qualificationList.add("M.Com");
        qualificationList.add("MSc / MFin / MS");
        qualificationList.add("BCom (Hons)");
        qualificationList.add("PGD Finance");
        qualificationList.add("BCA");
        qualificationList.add("B.IT");
        qualificationList.add("BCS");
        qualificationList.add("BA Computer Science");
        qualificationList.add("MCA");
        qualificationList.add("PGDCA");
        qualificationList.add("IT Diploma");
        qualificationList.add("ADIT");
        qualificationList.add("B.Sc");
        qualificationList.add("M.Sc");
        qualificationList.add("BSc (Hons)");
        qualificationList.add("MBBS");
        qualificationList.add("BDS");
        qualificationList.add("BPT");
        qualificationList.add("BAMS");
        qualificationList.add("BHMS");
        qualificationList.add("B.Pharma");
        qualificationList.add("BVSc");
        qualificationList.add("BSN / BScN");
        qualificationList.add("MDS");
        qualificationList.add("MCh");
        qualificationList.add("M.D");
        qualificationList.add("M.S Medicine");
        qualificationList.add("MPT");
        qualificationList.add("DM");
        qualificationList.add("M.Pharma");
        qualificationList.add("MMVSc");
        qualificationList.add("MMed");
        qualificationList.add("PGD Medicine");
        qualificationList.add("BBA");
        qualificationList.add("BHM");
        qualificationList.add("BBM");
        qualificationList.add("MBA");
        qualificationList.add("PGDM");
        qualificationList.add("ABA");
        qualificationList.add("ADBus");
        qualificationList.add("BL / LLB");
        qualificationList.add("ML / LLM");
        qualificationList.add("LLB (Hons)");
        qualificationList.add("ALA");
        qualificationList.add("Ph.D");
        qualificationList.add("M.Phil");
        qualificationList.add("Bachelor");
        qualificationList.add("Master");
        qualificationList.add("Diploma");
        qualificationList.add("Honours");
        qualificationList.add("Doctorate");
        qualificationList.add("Associate");
        return qualificationList;
    }

    private void multipleSelectionMotherTongue() {
        multi_SelectionMotherTongue.setItems(getMotherTongueItems());
        multi_SelectionMotherTongue.setOnItemSelectedListener(new MultipleSelection.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, boolean isSelected, int position) {
//                Toast.makeText(MainActivity.this, "On Item selected : " + isSelected, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSelectionCleared() {
//                Toast.makeText(MainActivity.this, "All items are unselected", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private List getMotherTongueItems() {
        ArrayList<String> motherTongueList = new ArrayList<>();
//        for (char i = 'A'; i <= 'Z'; i++)
//            alphabetsList.add(Character.toString(i));
        motherTongueList.add("Hindi");
        motherTongueList.add("Marathi");
        motherTongueList.add("Punjabi");
        motherTongueList.add("Bengali");
        motherTongueList.add("Gujarati");
        motherTongueList.add("Telugu");
        motherTongueList.add("Urdu");
        motherTongueList.add("Kannada");
        motherTongueList.add("English");

        motherTongueList.add("Tamil");
        motherTongueList.add("Odia");
        motherTongueList.add("Marwari");
        motherTongueList.add("Arunachali");
        motherTongueList.add("Assamese");
        motherTongueList.add("Awadhi");
        motherTongueList.add("Bhojpuri");
        motherTongueList.add("Chattisgarhi");
        motherTongueList.add("Haryanavi");

        motherTongueList.add("Himachali/Pahari");
        motherTongueList.add("Kashmiri");
        motherTongueList.add("Malayalam");
        motherTongueList.add("Khandesi");
        motherTongueList.add("Manipuri");
        motherTongueList.add("Rajasthani");
        motherTongueList.add("Sanskrit");
        motherTongueList.add("Sindhi");
        motherTongueList.add("Other");


        return motherTongueList;
    }

    private void multipleMaritalStatusSelectionCheckBox() {


        // initialize selected language array
        selectedMaritalStatus = new boolean[MaritalStatusArray.length];

        tvMultipleMaritalStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Initialize alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                // set title
                builder.setTitle("Select Marital Status");

                // set dialog non cancelable
                builder.setCancelable(false);

                builder.setMultiChoiceItems(MaritalStatusArray, selectedMaritalStatus, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        // check condition
                        if (b) {
                            // when checkbox selected
                            // Add position  in lang list
                            MaritalStatusList.add(i);
                            // Sort array list
                            Collections.sort(MaritalStatusList);
                        } else {
                            // when checkbox unselected
                            // Remove position from langList
                            MaritalStatusList.remove(Integer.valueOf(i));
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Initialize string builder
                        StringBuilder stringBuilder = new StringBuilder();
                        // use for loop
                        for (int j = 0; j < MaritalStatusList.size(); j++) {
                            // concat array value
                            stringBuilder.append(MaritalStatusArray[MaritalStatusList.get(j)]);
                            // check condition
                            if (j != MaritalStatusList.size() - 1) {
                                // When j value  not equal
                                // to lang list size - 1
                                // add comma
                                stringBuilder.append(", ");
                            }
                        }
                        // set text on textView
                        tvMultipleMaritalStatus.setText(stringBuilder.toString());
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // dismiss dialog
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // use for loop
                        for (int j = 0; j < selectedMaritalStatus.length; j++) {
                            // remove all selection
                            selectedMaritalStatus[j] = false;
                            // clear language list
                            MaritalStatusList.clear();
                            // clear text view value
                            tvMultipleMaritalStatus.setText("");
                        }
                    }
                });
                // show dialog
                builder.show();
            }
        });
    }


    private void SpinnerAge() {

        // use for loop
        for(int i=18;i<=70;i++)
        {
            // add values in price list
            AgeList.add(" "+i+" Years");
            // check condition
            if(i>1)
            {
                // Not include first value  in max list
                maxList.add(i+" Years");
            }

            if(i!=70)
            {
                // Not include  last value in min list
                minList.add(i+" Years");
            }
        }

        // Initialize adapter
        minAdapter=new ArrayAdapter<>(getContext(),R.layout.dropdown_item,minList);
        maxAdapter=new ArrayAdapter<>(getContext(),R.layout.dropdown_item,maxList);

        // set adapter
        spMin.setAdapter(minAdapter);
        spMax.setAdapter(maxAdapter);

        // set item selected listener on min spinner
        spMin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // clear max list
                maxList.clear();
                // use for loop
                for(int i = position+1; i< AgeList.size(); i++)
                {
                    // add values in max list
                    maxList.add(AgeList.get(i));
                }
                // notify adapter
                maxAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void multipleReligionSelectionCheckBox() {

        // initialize selected language array
        selectedLanguage = new boolean[ReligionArray.length];

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Initialize alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                // set title
                builder.setTitle("Select Language");

                // set dialog non cancelable
                builder.setCancelable(false);

                builder.setMultiChoiceItems(ReligionArray, selectedLanguage, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        // check condition
                        if (b) {
                            // when checkbox selected
                            // Add position  in lang list
                            religionList.add(i);
                            // Sort array list
                            Collections.sort(religionList);
                        } else {
                            // when checkbox unselected
                            // Remove position from langList
                            religionList.remove(Integer.valueOf(i));
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Initialize string builder
                        StringBuilder stringBuilder = new StringBuilder();
                        // use for loop
                        for (int j = 0; j < religionList.size(); j++) {
                            // concat array value
                            stringBuilder.append(ReligionArray[religionList.get(j)]);
                            // check condition
                            if (j != religionList.size() - 1) {
                                // When j value  not equal
                                // to lang list size - 1
                                // add comma
                                stringBuilder.append(", ");
                            }
                        }
                        // set text on textView
                        textView.setText(stringBuilder.toString());
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // dismiss dialog
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // use for loop
                        for (int j = 0; j < selectedLanguage.length; j++) {
                            // remove all selection
                            selectedLanguage[j] = false;
                            // clear language list
                            religionList.clear();
                            // clear text view value
                            textView.setText("");
                        }
                    }
                });
                // show dialog
                builder.show();
            }
        });
    }

// dropDown With Search
    private List getItems() {
        ArrayList<String> countryList = new ArrayList<>();
//        for (char i = 'A'; i <= 'Z'; i++)
//            alphabetsList.add(Character.toString(i));
       return countryList;
    }

}
