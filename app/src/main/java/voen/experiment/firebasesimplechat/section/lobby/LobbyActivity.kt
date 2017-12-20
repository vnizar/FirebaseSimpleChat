package voen.experiment.firebasesimplechat.section.lobby

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_lobby.*
import kotlinx.android.synthetic.main.toolbar.view.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.appcompat.v7.linearLayoutCompat
import org.jetbrains.anko.appcompat.v7.tintedEditText
import org.jetbrains.anko.customView
import org.jetbrains.anko.okButton
import org.jetbrains.anko.toast
import voen.experiment.firebasesimplechat.R
import voen.experiment.firebasesimplechat.base.BaseActivity
import voen.experiment.firebasesimplechat.helper.SharedPreferenceHelper
import voen.experiment.firebasesimplechat.helper.getFirebaseInstanceId
import voen.experiment.firebasesimplechat.model.ChatRoom
import voen.experiment.firebasesimplechat.section.login.LoginActivity
import voen.experiment.firebasesimplechat.section.main.GeneralRecyclerViewAdapter
import voen.experiment.firebasesimplechat.section.main.MainActivity
import voen.experiment.firebasesimplechat.utils.cutBy
import voen.experiment.firebasesimplechat.utils.hideVisibility

/**
 * Created by voen on 12/12/17.
 */
class LobbyActivity : BaseActivity() {
    private val roomReference by lazy {
        firebaseDatabase.reference.child("chats")
    }
    private val rooms: MutableList<ChatRoom> = mutableListOf()
    private val roomAdapter by lazy {
        GeneralRecyclerViewAdapter(R.layout.room_item, rooms as MutableList<Any>, {
            goToChatRoom((it as ChatRoom).roomName)
        },{
            chatRoom, view ->
                with(chatRoom as ChatRoom) {
                    view.findViewById<TextView>(R.id.tv_room_name).text = this.roomName
                    view.findViewById<TextView>(R.id.tv_last_chat).text = this.roomDescription.cutBy(30)
                }
        })
    }
    private val eventListener by lazy {
        object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot?) {
                pb_loading.hideVisibility()
                snapshot?.let {
                    rooms.clear()
                    it.children.forEach { it.getValue(ChatRoom::class.java)?.let { rooms.add(it) } }
                    if (rooms.isNotEmpty()) rv_rooms.smoothScrollToPosition(rooms.size - 1)
                }
                roomAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError?) {
                pb_loading.hideVisibility()
                error?.message?.let { toast(it) }
                error?.toException()?.printStackTrace()
            }
        }
    }

    override fun setupLayout() {
        setContentView(R.layout.activity_lobby)

        val toolbarView = toolbar as Toolbar
        setSupportActionBar(toolbarView)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbarView.toolbar_title.text = getString(R.string.main_menu_title)
    }

    override fun onActivityCreated() {
        initializeListener()

        with(rv_rooms) {
            adapter = roomAdapter
            layoutManager = LinearLayoutManager(this@LobbyActivity)
        }
        getTokenId()
    }

    private fun getTokenId() {
        getFirebaseInstanceId().token?.let {
            SharedPreferenceHelper.setString(SharedPreferenceHelper.FIREBASE_TOKEN, it)
        }
    }

    private fun goToChatRoom(roomId: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(MainActivity.INTENT_KEY_ROOM_ID, roomId)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.lobby_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.logout -> doLogout()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun doLogout(): Boolean {
        roomReference.removeEventListener(eventListener)
        firebaseDatabase.goOffline()
        firebaseAuth.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
        return true
    }

    private fun initializeListener() {
        fab_add_room.setOnClickListener {
            alert {
                title = getString(R.string.new_room)
                customView {
                    linearLayoutCompat {
                        orientation = LinearLayout.VERTICAL
                        val etRoomName = tintedEditText {
                            hint = context.getString(R.string.room_name)
                        }
                        val etRoomDesc = tintedEditText {
                            minLines = 3
                            hint = context.getString(R.string.room_desc)
                        }
                        okButton {
                            val roomName = etRoomName.text.toString()
                            if(roomName.isNotEmpty()) {
                                roomReference.child(roomName).setValue(ChatRoom(roomName, etRoomDesc.text.toString(), 1))
                            } else {
                                toast(context.getString(R.string.empty_room))
                            }
                        }
                    }
                }
            }.show()
        }
        roomReference.addValueEventListener(eventListener)
    }
}