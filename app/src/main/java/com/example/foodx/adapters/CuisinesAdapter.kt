package com.example.foodx.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodx.databinding.CuisineItemRvBinding
import com.example.foodx.models.CategoryMeals
import com.example.foodx.models.Cuisine

class CuisinesAdapter : RecyclerView.Adapter<CuisinesAdapter.CuisinesAdapterViewHolder>() {

    inner class CuisinesAdapterViewHolder(val binding: CuisineItemRvBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var cuisineList = ArrayList<Cuisine>()
    lateinit var onItemClick:((Cuisine) -> Unit)


    fun setCuisineList(cuisineList: List<Cuisine>) {
        this.cuisineList = cuisineList as ArrayList<Cuisine>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CuisinesAdapterViewHolder {
        return CuisinesAdapterViewHolder(
            CuisineItemRvBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CuisinesAdapterViewHolder, position: Int) {
        val cuisine = cuisineList[position]
        holder.binding.rvCuisineName.text = cuisine.strArea
        //Image
        val animationUrl = getAnimationUrl(cuisine.strArea)
        holder.binding.imageCountryFlag.setAnimationFromUrl(animationUrl)

        holder.itemView.setOnClickListener {
            onItemClick.invoke(cuisine)
        }
    }

    override fun getItemCount(): Int {
        return cuisineList.size
    }

    private fun getAnimationUrl(strArea: String): String {
        // Map the cuisine area to the corresponding JSON URL
        return when (strArea) {
            "American" -> "https://assets8.lottiefiles.com/packages/lf20_vf2Men.json"
            "British" -> "https://assets5.lottiefiles.com/packages/lf20_hk3pA4.json"
            "Canadian" -> "https://assets1.lottiefiles.com/packages/lf20_hlfjaudz.json"
            "Chinese" -> "https://assets10.lottiefiles.com/packages/lf20_3inndxc7.json"
            "Croatian" -> "https://assets3.lottiefiles.com/packages/lf20_e0MCujPX0x.json"
            "Dutch" -> "https://assets6.lottiefiles.com/packages/lf20_z3wamony.json"
            "Egyptian" -> "https://assets1.lottiefiles.com/packages/lf20_lmzlljzo.json"
            "Filipino" -> "https://assets10.lottiefiles.com/packages/lf20_nac8vvgu.json"
            "French" -> "https://assets10.lottiefiles.com/packages/lf20_idcdzbij.json"
            "Greek" -> "https://assets5.lottiefiles.com/packages/lf20_YC7e81Rd8c.json"
            "Indian" -> "https://assets2.lottiefiles.com/packages/lf20_Ibb89n.json"
            "Irish" -> "https://assets2.lottiefiles.com/packages/lf20_dujyqkem.json"
            "Italian" -> "https://assets7.lottiefiles.com/packages/lf20_1ybf5iqh.json"
            "Jamaican" -> "https://assets9.lottiefiles.com/packages/lf20_58xdD2x4uL.json"
            "Japanese" -> "https://assets10.lottiefiles.com/packages/lf20_LzHQb2.json"
            "Kenyan" -> "https://assets5.lottiefiles.com/packages/lf20_b916EHQ2Po.json"
            "Malaysian" -> "https://assets8.lottiefiles.com/packages/lf20_MJmUNc.json"
            "Mexican" -> "https://assets4.lottiefiles.com/packages/lf20_llpnmgts.json"
            "Moroccan" -> "https://assets8.lottiefiles.com/packages/lf20_y3VhypdG2a.json"
            "Polish" -> "https://assets3.lottiefiles.com/packages/lf20_1c2u5agj.json"
            "Portuguese" -> "https://assets4.lottiefiles.com/private_files/lf30_jxtfcl5y.json"
            "Russian" -> "https://assets1.lottiefiles.com/packages/lf20_Q7LwZnUXBq.json"
            "Spanish" -> "https://assets5.lottiefiles.com/packages/lf20_bSPcHWUL7x.json"
            "Thai" -> "https://assets9.lottiefiles.com/packages/lf20_9ErXHqdUmy.json"
            "Tunisian" -> "https://assets8.lottiefiles.com/packages/lf20_6adGImzoRn.json"
            "Turkish" -> "https://assets4.lottiefiles.com/packages/lf20_t0YUeJ0GoX.json"
            "Unknown" -> "https://assets3.lottiefiles.com/packages/lf20_kqacgm7o.json"
            "Vietnamese" -> "https://assets6.lottiefiles.com/packages/lf20_atbsursk.json"
            // Add more cases for other areas and their corresponding JSON URLs
            else -> "https://assets1.lottiefiles.com/packages/lf20_u7yrcwlk.json"
        }
    }
}