package com.example.animalcrossing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.animalcrossing.data.database.dataBaseProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import org.w3c.dom.Text

class RouteMapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var selectedRoute: PredefinedRoute

    private var pet: com.example.animalcrossing.data.entity.petEntity? = null
    private lateinit var walkerUser: com.example.animalcrossing.data.entity.userEntity
    private var walkerData: com.example.animalcrossing.data.entity.walkerEntity? = null
    private val db by lazy { dataBaseProvider.getDatabase(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedRoute = requireArguments().getSerializable("route") as PredefinedRoute

        pet = requireArguments().getSerializable("pet") as? com.example.animalcrossing.data.entity.petEntity
        walkerUser = requireArguments().getSerializable("walkerUser") as com.example.animalcrossing.data.entity.userEntity
        walkerData = requireArguments().getSerializable("walkerData") as? com.example.animalcrossing.data.entity.walkerEntity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       return inflater.inflate(R.layout.fragment_route_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        view.findViewById<TextView>(R.id.mapPetName).text = pet?.name ?: ""
        view.findViewById<TextView>(R.id.mapRouteName).text = selectedRoute.name
        view.findViewById<TextView>(R.id.mapWalkerName).text = walkerUser.name

        view.findViewById<Button>(R.id.buttonFinishWalk).setOnClickListener {
            val frag = Rating()
            val b = Bundle()
            b.putString("walkerEmail", walkerUser.userEmail)
            frag.arguments = b
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_contanier, frag)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.addPolyline(
            PolylineOptions().apply {
                addAll(selectedRoute.points.map { LatLng(it.latitude, it.longitude) })
                width(12f)
            }
        )

        val bounds = LatLngBounds.builder()
        selectedRoute.points.forEach { p ->
            bounds.include((LatLng(p.latitude, p.longitude)))
        }
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 150))
    }
}