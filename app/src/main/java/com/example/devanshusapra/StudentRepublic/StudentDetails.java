package com.example.devanshusapra.StudentRepublic;


public class StudentDetails {
    private String title;
    private String message;
    private String timestamp;

    public StudentDetails() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
//
//    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//    String UID = user.getUid();
//    FirebaseDatabase database1 = FirebaseDatabase.getInstance();
//    DatabaseReference mRootref = database1.getReference(
//            "users/" + UID);
//
//        mRootref.addValueEventListener(new ValueEventListener() {
//@Override
//public void onDataChange(DataSnapshot dataSnapshot) {
//        ClassName = dataSnapshot.child("class").getValue(String.class);
//        Log.i("name", ClassName);
//        }
//
//@Override
//public void onCancelled(DatabaseError databaseError) {
//        Log.e("error", databaseError.getMessage());
//        }
//        });