package com.app.guinote;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link cartas} factory method to
 * create an instance of this fragment.
 */
public class cartas extends Fragment {


    CarouselView carouselView;
    int[] sampleImages = {R.drawable.tapete2, R.drawable.tapete1, R.drawable.hierba, R.drawable.madera, R.drawable.football};
    ImageView fizqtapete, fdertapete, fizqreverso, fderreverso, fizqcartas,fdercartas, choosetapete, choosereverso, choosecartas;
    Integer idtapete = 0, idreverso=0, idcartas=0;
    public cartas() {
        super(R.layout.cartas);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.cartas, container, false);



        carouselView = view.findViewById(R.id.carouselView);
        carouselView.stopCarousel();
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);
        /*fizqtapete = (ImageView) getActivity().findViewById(R.id.tapeteflechaizda);
        fdertapete = (ImageView) getActivity().findViewById(R.id.tapeteflechadcha);
        fizqreverso = (ImageView) getActivity().findViewById(R.id.reversoflechaizda);
        fderreverso = (ImageView) getActivity().findViewById(R.id.reversoflechadcha);
        fizqcartas = (ImageView) getActivity().findViewById(R.id.cartasflechaizda);
        fdercartas = (ImageView) getActivity().findViewById(R.id.cartasflechadcha);
        choosetapete = (ImageView) getActivity().findViewById(R.id.choosetapete);
        choosereverso = (ImageView) getActivity().findViewById(R.id.choosereverso);
        choosecartas = (ImageView) getActivity().findViewById(R.id.choosecartas);*/

        return view;
    }


    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };

    public void openActivity1(){
        switch (idtapete){
            case 0:
                choosetapete.setImageResource(R.drawable.tapete2);
                break;
            case 1:
                choosetapete.setImageResource(R.drawable.tapete1);
                break;
            case 2:
                choosetapete.setImageResource(R.drawable.hierba);
                break;
            case 3:
                choosetapete.setImageResource(R.drawable.madera);
                break;
            case 4:
                choosetapete.setImageResource(R.drawable.football);
                break;
            case 5:
                choosetapete.setImageResource(R.drawable.tapete);
                break;
        }
    }
}