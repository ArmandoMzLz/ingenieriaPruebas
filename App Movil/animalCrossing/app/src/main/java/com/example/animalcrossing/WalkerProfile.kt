package com.example.animalcrossing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.animalcrossing.data.database.dataBaseProvider
import com.example.animalcrossing.data.entity.userEntity
import com.example.animalcrossing.data.entity.walkerRequestEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WalkerProfile : Fragment() {

    companion object {
        private const val ARG_WALKER = "walker"
        private const val ARG_ROUTE = "route"
        private const val ARG_PET_ID = "petId"
        private const val ARG_PET_NAME = "petName"
        private const val ARG_OWNER = "owner"

        fun newInstance(
            walker: userEntity,
            route: PredefinedRoute,
            petId: Int,
            petName: String,
            user: userEntity
        ): WalkerProfile {
            val fragment = WalkerProfile()
            val args = Bundle()

            args.putSerializable(ARG_WALKER, walker)
            args.putSerializable(ARG_ROUTE, route)
            args.putInt(ARG_PET_ID, petId)
            args.putString(ARG_PET_NAME, petName)
            args.putSerializable(ARG_OWNER, user)

            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var walker: userEntity
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
    ): View? {
        val view = inflater.inflate(R.layout.fragment_walker_profile, container, false)

        walker = arguments?.getSerializable(ARG_WALKER) as userEntity
        route = arguments?.getSerializable(ARG_ROUTE) as PredefinedRoute
        owner = arguments?.getSerializable(ARG_OWNER) as userEntity
        petId = arguments?.getInt(ARG_PET_ID) ?: -1
        petName = arguments?.getString(ARG_PET_NAME) ?: ""

        walkerName = view.findViewById(R.id.profileWalkerName)
        walkerEmail = view.findViewById(R.id.profileWalkerEmail)
        routeName = view.findViewById(R.id.profileRouteName)
        routeTime = view.findViewById(R.id.profileRouteTime)
        routePrice = view.findViewById(R.id.profileRoutePrice)
        confirmButton = view.findViewById(R.id.buttonConfirmWalk)

        walkerName.text = walker.name
        walkerEmail.text = walker.userEmail
        routeName.text = route.name
        routeTime.text = route.time
        routePrice.text = route.price

        confirmButton.setOnClickListener {
            createWalkRequest()
        }

        return view
    }

    private fun createWalkRequest() {
        val db = dataBaseProvider.getDatabase(requireContext())

        val request = walkerRequestEntity(
            walkerEmail = walker.userEmail,
            ownerEmail = owner.userEmail,
            petId = petId,
            petName = petName,
            routeName = route.name,
            status = "Pendiente"
        )

        CoroutineScope(Dispatchers.IO).launch {
            db.walkerRequestDao().insertRequest(request)

            launch(Dispatchers.Main) {
                Toast.makeText(requireContext(), "Solicitud enviada correctamente!", Toast.LENGTH_LONG).show()

                parentFragmentManager.popBackStack()
            }
        }
    }
}