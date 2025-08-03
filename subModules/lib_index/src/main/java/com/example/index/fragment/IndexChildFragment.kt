package com.example.index.fragment

import android.view.View
import com.example.common.base.BaseBindFragment
import com.example.common.constants.RoutePath
import com.example.common.ext.adapterOf
import com.example.common.ext.buildAdapter
import com.example.index.databinding.IndexFragmentIndexChildBinding
import com.example.index.databinding.IndexLayoutIndexChildItemBinding
import com.example.network.bean.HomeFeedItem
import com.example.network.bean.Recipe
import com.example.network.bean.User
import com.marky.route.annotation.Route

@Route(RoutePath.PAGE_INDEX_CHILD)
class IndexChildFragment : BaseBindFragment<IndexFragmentIndexChildBinding>(IndexFragmentIndexChildBinding::inflate) {

    private val list = mutableListOf<HomeFeedItem>(
        HomeFeedItem(
            User(
                "1",
                "Rhys",
                "https://avatars.githubusercontent.com/u/26372905?v=4",
                "",
                0,
                0,
                0
            ),
            Recipe(
                "1",
                "Test",
                "菜谱1",
                "https://avatars.githubusercontent.com/u/26372905?v=4",
                "菜谱1",
                "2022-01-01",
                1,
                3,
                4
            )
        ),
        HomeFeedItem(
            User(
                "1",
                "Rhys",
                "https://avatars.githubusercontent.com/u/26372905?v=4",
                "",
                0,
                0,
                0
            ),
            Recipe(
                "1",
                "Test",
                "菜谱2",
                "https://avatars.githubusercontent.com/u/26372905?v=4",
                "菜谱2",
                "2022-01-01",
                1,
                3,
                4
            )
        ),
        HomeFeedItem(
            User(
                "1",
                "Rhys",
                "https://avatars.githubusercontent.com/u/26372905?v=4",
                "",
                0,
                0,
                0
            ),
            Recipe(
                "1",
                "Test",
                "菜谱3",
                "https://avatars.githubusercontent.com/u/26372905?v=4",
                "菜谱3",
                "2022-01-01",
                1,
                3,
                4
            )
        ),
        HomeFeedItem(
            User(
                "1",
                "Rhys",
                "https://avatars.githubusercontent.com/u/26372905?v=4",
                "",
                0,
                0,
                0
            ),
            Recipe(
                "1",
                "Test",
                "菜谱4",
                "https://avatars.githubusercontent.com/u/26372905?v=4",
                "菜谱4",
                "2022-01-01",
                1,
                3,
                4
            )
        ),
        HomeFeedItem(
            User(
                "1",
                "Rhys",
                "https://avatars.githubusercontent.com/u/26372905?v=4",
                "",
                0,
                0,
                0
            ),
            Recipe(
                "1",
                "Test",
                "菜谱5",
                "https://avatars.githubusercontent.com/u/26372905?v=4",
                "菜谱5",
                "2022-01-01",
                1,
                3,
                4
            )
        ),
        HomeFeedItem(
            User(
                "1",
                "Rhys",
                "https://avatars.githubusercontent.com/u/26372905?v=4",
                "",
                0,
                0,
                0
            ),
            Recipe(
                "1",
                "Test",
                "菜谱6",
                "https://avatars.githubusercontent.com/u/26372905?v=4",
                "菜谱6",
                "2022-01-01",
                1,
                3,
                4
            )
        )
    )

    override fun initView(binding: IndexFragmentIndexChildBinding) {
        val adapter = adapterOf <HomeFeedItem, IndexLayoutIndexChildItemBinding>{ holder, position, item ->
            holder.binding.apply {
                tvTitle.text = item.recipe.name
                tvDesc.text = item.recipe.desc
                tvLikes.text = item.recipe.likes.toString()
                tvComments.text = item.recipe.commentsCount.toString()
                tvName.text = item.user.name
//                ivPicture
            }
        }

        binding.rvIndex.adapter = adapter

        adapter.setNewInstance(list)
    }

    override fun initData(binding: IndexFragmentIndexChildBinding) {

    }

    override fun initTitleBar(): View? = null
}