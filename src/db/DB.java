package db;

import util.*;

import java.util.ArrayList;

public class DB {
    public static ArrayList<MemberTM> members = new ArrayList<>();
    public static ArrayList<CatalogueTM> books = new ArrayList<>();
    public static ArrayList<Category> categories = new ArrayList<>();
    public static ArrayList<IssueBookTM> issuedBooks = new ArrayList<>();
    public static ArrayList<ReturnBookTM> returnedBooks = new ArrayList<>();


    static {
        members.add(new MemberTM("MBR001","Charith","De Silva","Galle","983227554V","0112587543","cdesilva@gmail.com"));
        members.add(new MemberTM("MBR002","Suvin","Perera","Gampaha","872375322V","0345249920","su.perera@yahoo.com"));
        members.add(new MemberTM("MBR003","Deshan","Dias","Colombo","99792448V","0824342351","diasdeshan99@yahoo.com"));
        members.add(new MemberTM("MBR004","Gayan","Pathirana","Kalutara","92835021V","0256638951","gaypathi21@yahoo.com"));
        members.add(new MemberTM("MBR005","Sumudu","Upatissa","Matara","7203279134V","0643112766","iamsupatissa@yahoo.com"));
    }

    static {
        books.add(new CatalogueTM("BK001","Robin Hood","Henry Douglas","History","St.Martin",570.00,"Not Available"));
        books.add(new CatalogueTM("BK002","Human Biology","Samuel Jensen","Educational","Oxford",2050.00,"Available"));
        books.add(new CatalogueTM("BK003","CheckMate - Ultimate Guide","Ivan Romanoff","Sport","Cremlin",1225.00,"Available"));
        books.add(new CatalogueTM("BK004","Nightflyers","George RR Martin","Science-Fiction","BloodMoon",800.00,"Available"));
        books.add(new CatalogueTM("BK005","Famous Five: Mystery of Devil's Rock","Enid Blyton","Adventure","Vauxall",430.00,"Available"));
    }

    static {
        categories.add(new Category("Action"));
        categories.add(new Category("Adventure"));
        categories.add(new Category("Art"));
        categories.add(new Category("History"));
        categories.add(new Category("Science-Fiction"));
        categories.add(new Category("Literature"));
        categories.add(new Category("Sport"));
        categories.add(new Category("Educational"));
    }

    static {
        issuedBooks.add(new IssueBookTM("2019-08-01","IB001","MBR001","Charith De Silva","BK001","Robin Hood","Pending"));
    }


}
