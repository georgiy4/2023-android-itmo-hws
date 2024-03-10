package ru.ok.itmo.example

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.gson.GsonBuilder
import com.google.gson.internal.bind.MapTypeAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.SharedPreferences
import android.database.CursorWindow
import android.widget.CursorAdapter
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.withContext
import okhttp3.internal.notifyAll
import retrofit2.http.Path
import java.util.UUID

class MainFragment : Fragment(R.layout.fragment) {

    companion object {
        private val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(Api.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val api = retrofit.create(Api::class.java)
    }

    private var userName: String? = null
    private var openChannel: Boolean = false
    private var token: String? = null

    private fun setLoad() {
        requireView().findViewById<TextView>(R.id.list_chat).isVisible = false
        requireView().findViewById<TextView>(R.id.progress_bar).isVisible = true
        requireView().findViewById<TextView>(R.id.text_message).isVisible = false
    }

    private fun setMessage(message: String) {
        val textMessage = requireView().findViewById<TextView>(R.id.text_message)
        textMessage.text = message
        textMessage.isVisible = true
    }

    private fun showMessage(message: String) {
        Toast.makeText(
            requireActivity(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun setShowList(adapter: ArrayViewAdapter) {
        val list = requireActivity().findViewById<ListView>(R.id.list_chat)
        list.adapter = adapter
        list.isVisible = true
    }

    fun login(token: String, user: String) {
        this.token = token
        this.userName = user
    }

    private fun logout(){
        val token = this.token
        CoroutineScope(Dispatchers.IO).launch {
            if (token != null) {
                api.logout(token)
            }
        }
    }

    fun openChannels() {
        view?.findViewById<TextView>(R.id.header)?.text = "Chats"

        var pref = requireActivity().getSharedPreferences("TABLE", Context.MODE_PRIVATE)

        //pref.edit().
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                setLoad()
            }

            try {
                val data = api.getAllChannels()
                withContext(Dispatchers.Main) {
                    if (data.isEmpty()) {
                        setMessage("There is no data")
                    } else {
                        val adapter = ArrayViewAdapter(arrayListOf())
                        for (channel in data) {
                            addChatItem(channel, adapter)
                        }
                        setShowList(adapter)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    setMessage("Download error: " + e.message)
                }
            }
            withContext(Dispatchers.Main) {
                requireView().findViewById<TextView>(R.id.progress_bar).isVisible = false
            }
        }
    }

    private fun addChatItem(channel: String, adapter: ArrayViewAdapter){
        val chatItem = requireActivity().layoutInflater.inflate(R.layout.chat_item, null, true)
        chatItem.findViewById<TextView>(R.id.textView).text = channel
        chatItem.setOnClickListener{
            openChannel(channel)
            openChannel = true
        }
        chatItem.findViewById<ChatAvatarView>(R.id.icon).setChatName(channel)
        adapter.add(chatItem)
    }

    private fun openChannel(channel: String) {
        view?.findViewById<Button>(R.id.bt_create)?.isVisible = true
        view?.findViewById<TextView>(R.id.header)?.text = channel
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                setLoad()
            }

            try {
                val data = api.getChannelMessages(channel)
                withContext(Dispatchers.Main) {
                    if (data.isEmpty()) {
                        setMessage("There is no messages")
                    } else {
                        val adapter = ArrayViewAdapter(arrayListOf())
                        for (message in data) {
                            addMessageItem(message, adapter)
                        }
                        setShowList(adapter)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    setMessage("Download error: " + e.message)
                }
            }
            withContext(Dispatchers.Main) {
                requireView().findViewById<TextView>(R.id.progress_bar).isVisible = false
            }
        }
    }

    private fun addMessageItem(message: Message, adapter: ArrayViewAdapter){
        val messageItem = requireActivity().layoutInflater.inflate(R.layout.message_item, null, true)
        messageItem.findViewById<TextView>(R.id.message_text).text = message.data.text?.text
        messageItem.findViewById<TextView>(R.id.message_from).text = "From ${message.from}"
        adapter.add(messageItem)
    }

    fun postMessage(channel: String, text: String):Boolean {
        view?.findViewById<Button>(R.id.bt_create)?.isVisible = false
        val userName = this.userName
        val token = this.token
        return if (userName != null && token != null) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val message = Message(
                        123,
                        userName,
                        "${channel}@channel",
                        MessageData(TextData(text), null)
                    )
                    api.message(token, message)
                }catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        showMessage("Download error: " + e.message)
                    }
                }
            }
            true
        } else false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.bt_back).setOnClickListener {
            if (openChannel) {
                openChannels()
                openChannel = false
            } else {
                logout()
                parentFragmentManager.setFragmentResult("back", bundleOf())
            }
        }

        view.findViewById<Button>(R.id.bt_create).setOnClickListener {
            parentFragmentManager.setFragmentResult("start_create_chat", bundleOf())
        }
    }
}