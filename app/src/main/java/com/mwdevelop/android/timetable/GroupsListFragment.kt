package com.mwdevelop.android.timetable

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_day_pager.view.*
import kotlinx.android.synthetic.main.frament_groups_list.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import android.app.ProgressDialog
import android.app.Activity




/**
 * Created by mateusz on 18.03.18.
 */
class GroupsListFragment:Fragment() {

    lateinit var mGropusRecycleView:RecyclerView
    lateinit var mSearchView: SearchView
    private lateinit var GroupNameAdapter:GroupAdapter
    private lateinit var mListLinearLayout: LinearLayout
    private lateinit var mNoDataLinearLayout: LinearLayout
    private  var dateUpdate:String=""
    private lateinit var updateTextView: TextView
    companion object {

        fun newInstance():Fragment{
            return GroupsListFragment()
        }
    }

    private var groupsList=ArrayList<Group>()
    private var fGroupsList=ArrayList<Group>()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            var view=inflater!!.inflate(R.layout.frament_groups_list,container,false)
             mListLinearLayout=view.findViewById(R.id.LL_RV)
             mNoDataLinearLayout=view.findViewById(R.id.LL_NoData)
             DownloadGroupsNames().execute()


//            if(DownloadGroupsNames().execute().get()){
//                mListLinearLayout.visibility=View.GONE
//                mNoDataLinearLayout.visibility=View.VISIBLE
//            }
//            else{
//                mListLinearLayout.visibility=View.VISIBLE
//                mNoDataLinearLayout.visibility=View.GONE
//            }
            mGropusRecycleView=view.findViewById(R.id.list_recycle_view)
            mSearchView=view.findViewById(R.id.search_view)
            updateTextView=view.findViewById(R.id.TV_update)
            mGropusRecycleView.layoutManager=LinearLayoutManager(this!!.activity)
            updateUI()

            mSearchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                  GroupNameAdapter.filter!!.filter(newText)
                    return false
                }

            })
            return view



    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
    }

   private inner  class GroupHolder(inflater: LayoutInflater?,parent:ViewGroup):RecyclerView.ViewHolder(inflater!!.inflate(R.layout.list_item_gruop,parent,false)),View.OnClickListener{
       override fun onClick(v: View?) {
           var intent:Intent=DayPagerActivity.getActivity(mGroup.url,mGroup.name,context)
           startActivity(intent)
       }



       private lateinit var mNameGroupTextView:TextView
        private lateinit var mGroup:Group

        init {
            mNameGroupTextView=itemView.findViewById(R.id.TV_group_name)
            itemView.setOnClickListener(this)
        }

        fun bind(group:Group)
        {
            mGroup=group
            mNameGroupTextView.text=mGroup.name
        }

    }

    private inner class GroupAdapter:RecyclerView.Adapter<GroupHolder>(),Filterable{
         var valueFilter:ValueFilter?=null
        override fun getFilter(): Filter {
            if(valueFilter==null){
                valueFilter=ValueFilter()
            }
            return valueFilter as ValueFilter
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupHolder {
           var layoutInflater=LayoutInflater.from(activity)
            return GroupHolder(layoutInflater,parent)
        }

        override fun getItemCount(): Int {
           return groupsList.size
        }




        override fun onBindViewHolder(holder: GroupHolder?, position: Int) {
           var group=groupsList.get(position)
            holder!!.bind(group)
        }

        public inner class ValueFilter:Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                var resuts= FilterResults()

                if(constraint!=null && constraint.isNotEmpty())
                {
                    var filterList=ArrayList<Group>()
                    for(item in fGroupsList){
                        if(item.name.toLowerCase().contains(constraint.toString().toLowerCase()))
                        {
                            var tmp=Group()
                            tmp.name=item.name
                            tmp.url=item.url
                            filterList.add(tmp)
                        }
                    }
                    resuts.count=filterList.size
                    resuts.values=filterList
                }
                else
                {
                    resuts.count=fGroupsList.size
                    resuts.values=fGroupsList
                }
                return resuts
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                groupsList=results!!.values as ArrayList<Group>
                notifyDataSetChanged()
            }

        }

    }

    private inner class DownloadGroupsNames:AsyncTask<Void,Void,Boolean>(){
        lateinit var progressDialog:ProgressDialog
        override fun doInBackground(vararg params: Void): Boolean {
            Thread.sleep(3000)
            return try {
                geGroupstNames()
            } catch (e:Exception) {
                false
            }
        }
        override fun onPreExecute() {
            progressDialog = ProgressDialog(activity)
            progressDialog.setMessage(getString(R.string.load_data))
            progressDialog.isIndeterminate = false
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            progressDialog.setCancelable(true)
            progressDialog.show()
        }

        override fun onPostExecute(result: Boolean?) {
            progressDialog.dismiss()
            GroupNameAdapter.notifyDataSetChanged()
            if(result!!)
            {
                mListLinearLayout.visibility=View.VISIBLE
                mNoDataLinearLayout.visibility=View.GONE
                updateUI()
            }
            else
            {
                mListLinearLayout.visibility=View.GONE
                mNoDataLinearLayout.visibility=View.VISIBLE
            }


        }



    }

    @Throws(IOException::class)
    private fun geGroupstNames():Boolean {
        try {
            val groups = ArrayList<Group>()
            var doc = Jsoup.connect("https://wimii.pcz.pl/pl/plan-zajec").timeout(20000).validateTLSCertificates(false).get()
            val element = doc.select("article[about=/pl/plan-zajec] p").select("a").first()
            var nazwa=doc.select("article[about=/pl/plan-zajec] p").select("strong").first().text().removePrefix("(").removeSuffix(".)").removeSuffix(")")
            val link = element.attr("abs:href")
            doc = Jsoup.connect(link).timeout(20000).validateTLSCertificates(false).get() as Document
            val elements = doc.select("a")
            for (e in elements) {
                if (!e.text().isEmpty()) {
                    val tmp = Group()
                    tmp.name = e.text()
                    tmp.url = e.attr("abs:href")
                    groups.add(tmp)
                }
            }
            groupsList = groups
            fGroupsList=groups
            dateUpdate=nazwa
            return true
        }
        catch (e:Exception)
        {
            return false
        }
    }

    fun updateUI(){
        GroupNameAdapter=GroupAdapter()
        mGropusRecycleView.adapter=GroupNameAdapter
        updateTextView.text=dateUpdate
    }
}