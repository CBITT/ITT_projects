/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mongoproject;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.MapReduceAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bson.Document;






/**
 *
 * @author BangoCs
 */
public class Mongo3 {
    
     
    
    
public static final MongoClient client = new MongoClient("localhost", 27017);   
 public static final MongoDatabase db = client.getDatabase("analytics_app");
  public static  MongoCollection<Document> coll_users = db.getCollection("coll_users");
         public static MongoCursor <Document> cursor;
 

public static Document result;
    private static Object MongoDBManager;

  
 public static void read_all_docs (MongoCursor <Document> cursor){
 
     setCursor(cursor);

            try {
                while (cursor.hasNext()) {
                
                    System.out.println(cursor.next().toJson());
                 
                   }

            } finally {
                cursor.close();
                
            }
            
 } 
 
 
    public static void create_one_user(String firstname, String surname, String email, String phone, boolean okToContact) {
     //coll_users.drop();    
   
    Random rand = new Random();
        int mouse_clicks = rand.nextInt(200);    
        int winter_tab_clicks = rand.nextInt(20);    
        int summer_tab_clicks = rand.nextInt(50);    
        int new_arrival_clicks = rand.nextInt(100);    
        int summer_collection_clicks = rand.nextInt(100);    
     
        
    Document document = new Document();
   document.put("firstname", firstname);
   document.put("surname", surname);
   document.put("email", email);
   document.put("phone", phone);
   document.put("okToContact",okToContact);
   

   // creating objects to fit in the document   
   // create btn_selection object
   
    BasicDBObject clearance_products = new BasicDBObject();
    clearance_products.put("winter_tab_clicks", winter_tab_clicks);
    clearance_products.put("summer_tab_clicks", summer_tab_clicks);
    
     BasicDBObject male_products = new BasicDBObject();
    male_products.put("new_arrivals_tab_clicks", new_arrival_clicks);
    male_products.put("summer_collection_tab_clicks", summer_collection_clicks);

   
    BasicDBObject female_products = new BasicDBObject();
    female_products.put("new_arrivals_tab_hover", new_arrival_clicks);
    female_products.put("summer_collection_tab_hover",summer_collection_clicks);
   
    BasicDBObject btn_selection = new BasicDBObject();
    btn_selection.append("clearence_products", clearance_products);
    btn_selection.append("male_products", male_products);
    btn_selection.append("female_products", female_products);
    

   
//create social_media1 LIst
    BasicDBObject social_media1 = new BasicDBObject();
    social_media1.put("name", "facebook");
    social_media1.put("sponsored", true);
    
//create social_media2 list
     BasicDBObject social_media2 = new BasicDBObject();
    social_media2.put("name", "twitter");
    social_media2.put("sponsored", false);
    
   List<BasicDBObject> social_media_list = new ArrayList<BasicDBObject>(); 
   social_media_list.add(social_media1);
   social_media_list.add(social_media2); 
   
 //create website_name   
    BasicDBObject website_names = new BasicDBObject();
    website_names.put("name", "www.test.com");
  
  //create mouse_click object     
     BasicDBObject mouse_click = new BasicDBObject();
     mouse_click.put("mouse_click", mouse_clicks);
    
//create interactions object
  
     BasicDBObject interactions = new BasicDBObject();

//fit all objects and object lists into interactions
    interactions.append("btn_selection",btn_selection);
    interactions.append("social_media_list",social_media_list);
    interactions.append("website_names",website_names);
    interactions.append("clicks",mouse_click); 
  
    //add O1 to document and insert
    document.put("interactions", interactions);
    
   
   coll_users.insertOne(document); 
 
   MongoCursor<Document> cursor = coll_users.find().iterator(); 
     read_all_docs(cursor);
 
    }
    
    public static void create_many_users(){
       

// create many
        coll_users.drop();
   List<Document> users = new ArrayList <Document> ();
   
   Document user1 = new Document();
   Document user2 = new Document();
   
   user1.append("firstname", "joe" )
                .append("surname", "smith" )
                .append("email", "js@fdfd.com")
                .append("phone", "0156464446")
                .append("okToContact",false);
             
     user2.append("firstname", "mary" )
                .append("surname", "obrien" )
                .append("email", "mob@fdfewfrd.com")
                .append("phone", "0154444446")
                .append("okToContact",true); 
   
 users.add(user1);
 users.add(user2);
 
   
   coll_users.insertMany(users);
   
   System.out.println("Collection size: " + coll_users.count() + " documents. \n");
    }
    public static void update_one_user(String updateBySurname,String firstname, String surname, String email, String phone, boolean okToContact)
    {
    //update 1
   
     Document docToUpdate = coll_users.find(Filters.eq("surname", updateBySurname)).first();       
       
     coll_users.updateOne(docToUpdate,
                    new Document("$set", new Document("firstname", firstname)
                            .append("surname", surname)
                            .append("email", email)
                            .append("phone", phone)
                            .append("okToContact", okToContact))
            
     
     );
     System.out.print("Print updated version\n");
      MongoCursor<Document> cursor2 = coll_users.find().iterator();
     read_all_docs(cursor2);
    
    }
    public static void update_many_users(){
        
        //update many
        coll_users.updateMany(new Document("okToContact", true),
                new Document("$set", new Document("contacted", false)));
     System.out.println("Update many results\n");
      MongoCursor<Document> cursor3 = coll_users.find().iterator(); 
     read_all_docs(cursor3);
    }
    
    
     public static void delete_one_user(String deleteBySurname)
    {
    //delete 1
     
     coll_users.deleteOne(new Document ("surname",deleteBySurname));
    System.out.println("Deleted 1 user\n");
     MongoCursor<Document> cursor = coll_users.find().iterator(); 
     read_all_docs(cursor);
    }
   private static void delete_many_users()  
     {
      //delete many
     coll_users.deleteMany(new Document ("contacted",true));
    System.out.println("Deleted all who is been contacted\n");
     MongoCursor<Document> cursor5 = coll_users.find().iterator(); 
     read_all_docs(cursor5);
     }

   public static void setCursor(MongoCursor<Document> cursr){
   cursor = cursr;
   }
   
   public static MongoCursor<Document> getCursor() {
  
          return cursor;
        } 

    public static void mapreduce() {
        String map = "function(){"+
                            "emit (this.okToContact,1); "+
                            "};";
        
        String reduce = "function (okToContact, count)"+
                                "{return Array.sum(count);"+
                                "};";
        

        
         coll_users.mapReduce(map, reduce)
        .action(MapReduceAction.REPLACE)
        .collectionName("how_many_ok_to_contact");
         

  FindIterable<Document> res = coll_users.find();
  MongoCursor<Document> cursor = res.iterator();

    while(cursor.hasNext()){
        Document current = cursor.next();
        System.out.println(current.toString());
    }
    cursor.close();
    
    }
   
  

   
}



/*
code references:
https://www.kenwalger.com/blog/nosql/mongodb-and-java/
http://hmkcode.com/mongodb-java-mapreduce/
for mapreduce:
https://stackoverflow.com/questions/43443611/unexpected-map-reduce-result-with-mongodb-3-0
for crud:
http://abhijitbashetti.blogspot.ie/2013/11/embeddedsub-document-example-of-mongodb.html
https://stackoverflow.com/questions/12272121/mongodb-java-to-insert-embedded-document



*/
