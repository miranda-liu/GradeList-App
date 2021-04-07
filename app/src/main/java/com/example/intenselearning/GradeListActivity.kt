package com.example.intenselearning

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder


class GradeListActivity : AppCompatActivity() {
    companion object{
        val TAG = "GradeListActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grade_list)

        // TODO: make a whole recyclerview layout and stuff for the grades

        // for now, log a list of all the grades
        Backendless.Data.of(Grade::class.java).find(object : AsyncCallback<List<Grade?>?> {
                override fun handleResponse(foundGrades: List<Grade?>?) {
                    // all Grade instances have been found

                    // get the current user's objectId
                   val userId = Backendless.UserService.CurrentUser().userId

                    // make a temporary list for just our matches
                    val matchingList = mutableListOf<Grade?>()
                    if (foundGrades != null){
                        for(grade in foundGrades)
                            if(grade?.ownerId == userId){
                                matchingList.add(grade)
                            }
                    }

                    Log.d(TAG, "handleResponse:" + matchingList.toString())
                }

                override fun handleFault(fault: BackendlessFault) {
                    // an error has occurred, the error code can be retrieved with fault.getCode()
                    Log.d(TAG, "handleFault:" + fault.message)
                }
            })


        Backendless.Data.of(Grade::class.java).find(object : DataQueryBuilder query, AsyncCallback<List<Grade?>?> {
            override fun handleResponse(foundGrades: List<Grade?>?) {
                // all Grade instances have been found
                Log.d(TAG, "handleResponse:" + foundGrades.toString())
            }

            override fun handleFault(fault: BackendlessFault) {
                // an error has occurred, the error code can be retrieved with fault.getCode()
                Log.d(TAG, "handleFault:" + fault.message)
            }
        })
    }
}