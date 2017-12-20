package voen.experiment.firebasesimplechat.section.main

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.view.*
import org.jetbrains.anko.toast
import voen.experiment.firebasesimplechat.R
import voen.experiment.firebasesimplechat.base.BaseActivity
import voen.experiment.firebasesimplechat.helper.SharedPreferenceHelper
import voen.experiment.firebasesimplechat.model.Chat
import voen.experiment.firebasesimplechat.utils.hideVisibility

class MainActivity : BaseActivity() {
    private val chatReference by lazy {
        firebaseDatabase.reference.child("chats").child(roomId).child("messages")
    }
    private val subscribeReference by lazy {
        firebaseDatabase.reference.child("subscribers").child(roomId)
    }
    private val messages: MutableList<Chat> = mutableListOf()
    private val messageAdapter by lazy {
        GeneralRecyclerViewAdapter(R.layout.chat_item, messages as MutableList<Any>, {
            chatReference.child((it as Chat).id).removeValue()
        }, { chat, view ->
            with(chat as Chat) {
                view.findViewById<TextView>(R.id.tv_message).text = this.message
                view.findViewById<TextView>(R.id.tv_from).text = this.from
            }
        })
    }
    private val roomId by lazy {
        intent.extras.getString(INTENT_KEY_ROOM_ID)
    }
    private val instanceId by lazy {
        SharedPreferenceHelper.getString(SharedPreferenceHelper.FIREBASE_TOKEN)
    }
    private val eventListener by lazy {
        object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot?) {
                pb_loading.hideVisibility()
                snapshot?.let {
                    messages.clear()
                    it.children.forEach {
                        it.getValue(Chat::class.java)?.let { messages.add(it) }
                        it.child("messages")
                    }
                    if (messages.isNotEmpty()) rv_messages.smoothScrollToPosition(messages.size - 1)
                }
                messageAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError?) {
                pb_loading.hideVisibility()
                error?.message?.let { toast(it) }
                error?.toException()?.printStackTrace()
            }
        }
    }
    private val subscribeListener by lazy {
        object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(snapshot: DataSnapshot?) {
                snapshot?.let {
                    menu?.findItem(R.id.subscribe)?.title = if(it.children.filter { it.value == instanceId }.isNotEmpty()) getString(R.string.unsubcribe) else getString(R.string.subscribe)
                }
            }

        }
    }
    private var menu: Menu? = null

    companion object {
        val INTENT_KEY_ROOM_ID = "room_id"
    }

    override fun setupLayout() {
        setContentView(R.layout.activity_main)

        val toolbarView = toolbar as Toolbar
        setSupportActionBar(toolbarView)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbarView.toolbar_title.text = getString(R.string.main_menu_title)
    }

    override fun onActivityCreated() {
        with(rv_messages) {
            adapter = messageAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        initializeListener()
    }

    private fun initializeListener() {
        bt_send.setOnClickListener {
            chatReference.push().key.let {
                chatReference.child(it).setValue(Chat(it, et_message.text.toString(), SharedPreferenceHelper.getString(SharedPreferenceHelper.FIREBASE_USER_NAME)))
            }
            et_message.text.clear()
        }
        chatReference.addValueEventListener(eventListener)
        subscribeReference.addValueEventListener(subscribeListener)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        this.menu = menu

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.subscribe -> subscribeRoom()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun subscribeRoom(): Boolean {
        instanceId?.let {
            val token = it
            subscribeReference.push().key.let {
                subscribeReference
                        .child(it)
                        .setValue(token)
            }
        }

        return true
    }


}
