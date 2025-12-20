package com.example.animalcrossing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.animalcrossing.data.PredefinedRoute
import com.example.animalcrossing.data.database.dataBaseProvider
import com.example.animalcrossing.data.entity.userEntity
import com.example.animalcrossing.data.entity.walkerRequestEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WalkerProfile : Fragment() {

    companion object {
        private const val ARG_WALKER = "walker"
        private const val ARG_ROUTE = "route"
        private const val ARG_PET_ID = "petId"
        private const val ARG_PET_NAME = "petName"
        private const val ARG_USER = "user"
        private const val ARG_OWNER = "owner"

        fun newInstance(
            walkerEmail: String,
            route: PredefinedRoute,
            petId: Int,
            petName: String,
            user: userEntity
        ): WalkerProfile {
            val fragment = WalkerProfile()
            val args = Bundle().apply {
                putString(ARG_WALKER, walkerEmail)
                putSerializable(ARG_ROUTE, route)
                putInt(ARG_PET_ID, petId)
                putString(ARG_PET_NAME, petName)
                putSerializable(ARG_USER, user)
            }
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var walkerEmailValue: String
    private lateinit var route: PredefinedRoute
    private lateinit var owner: userEntity
    private var petId: Int = -1
    private var petName: String = ""

    private lateinit var walkerName: TextView
    private lateinit var walkerEmail: TextView
    private lateinit var routeName: TextView
    private lateinit var routeTime: TextView
    private lateinit var routePrice: TextView
    private lateinit var confirmButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_walker_profile, container, false)

        walkerEmailValue = requireArguments().getString(ARG_WALKER)
            ?: error("walkerEmail requerido")

        route = requireArguments().getSerializable(ARG_ROUTE) as PredefinedRoute
        owner = requireArguments().getSerializable(ARG_USER) as userEntity
        petId = requireArguments().getInt(ARG_PET_ID)
        petName = requireArguments().getString(ARG_PET_NAME) ?: ""

        walkerName = view.findViewById(R.id.profileWalkerName)
        walkerEmail = view.findViewById(R.id.profileWalkerEmail)
        routeName = view.findViewById(R.id.profileRouteName)
        routeTime = view.findViewById(R.id.profileRouteTime)
        routePrice = view.findViewById(R.id.profileRoutePrice)

        loadWalkerInfo()

        routeName.text = route.name
        routeTime.text = route.time
        routePrice.text = route.price

        confirmButton = view.findViewById(R.id.buttonConfirmWalk)
        confirmButton.setOnClickListener {
            createWalkRequest()
        }

        return view
    }


    private fun loadWalkerInfo() {
        val db = dataBaseProvider.getDatabase(requireContext())

        lifecycleScope.launch(Dispatchers.IO) {

            val walkerUser = db.userDao().getUserById(walkerEmailValue)
            val walkerRating = db.walkerDao().getWalkerByEmail(walkerEmailValue)

            val name = walkerUser?.name ?: "Paseador"
            val email = walkerUser?.userEmail ?: walkerEmailValue

            val ratingText = walkerRating?.ratingAverage?.let {
                "⭐ %.1f".format(it)
            } ?: "Sin calificaciones"

            withContext(Dispatchers.Main) {
                walkerName.text = name
                walkerEmail.text = email
                view?.findViewById<TextView>(R.id.walkerRating)?.text = ratingText
            }
        }
    }



    private fun createWalkRequest() {
        val db = dataBaseProvider.getDatabase(requireContext())

        val request = walkerRequestEntity(
            walkerEmail = walkerEmailValue,
            ownerEmail = owner.userEmail,
            petId = petId,
            petName = petName,
            routeName = route.name,
            status = "Pendiente"
        )

        CoroutineScope(Dispatchers.IO).launch {
            db.walkerRequestDao().insertRequest(request)

            launch(Dispatchers.Main) {
                Toast.makeText(
                    requireContext(),
                    "¡Solicitud enviada correctamente!",
                    Toast.LENGTH_LONG
                ).show()

                parentFragmentManager.popBackStack()
            }
        }
    }
}