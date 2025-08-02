package com.rhys.chat

import com.example.common.base.BaseBindFragment
import com.example.common.constants.RoutePath
import com.example.common.utils.LogUtils
import com.example.common.view.TitleBar
import com.marky.route.annotation.Route
import com.rhys.chat.databinding.ChatFragmentChatBinding

@Route(RoutePath.PAGE_CHAT)
class ChatFragment : BaseBindFragment<ChatFragmentChatBinding>(ChatFragmentChatBinding::inflate) {


    override fun initTitleBar(): TitleBar {
        return binding.titleBar
    }

    override fun initView(binding: ChatFragmentChatBinding) {
        LogUtils.d("ChatFragment 初始化成功")
    }

    override fun initData(binding: ChatFragmentChatBinding) {

    }
}