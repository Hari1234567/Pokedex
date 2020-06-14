package com.example.pokedex;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class PaginationListener extends RecyclerView.OnScrollListener {
    public static final int PAGE_START=1;
    public static final int PAGE_SIZE=10;
    LinearLayoutManager linearLayoutManager;
    public PaginationListener(@NonNull LinearLayoutManager linearLayoutManager){
               this.linearLayoutManager=linearLayoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int visibleItemCount=linearLayoutManager.getChildCount();
        int totalItemCount=linearLayoutManager.getItemCount();
        int firstVisibleItemPosition=linearLayoutManager.findFirstVisibleItemPosition();

       if(!isLoading() && !isLastPage()){


            if(visibleItemCount+firstVisibleItemPosition>=totalItemCount &&firstVisibleItemPosition>=0){
              // mainActivity.printToast("asdad");
                loadMoreItems();
            }
        }
    }



    public abstract boolean isLoading();
    public abstract boolean isLastPage();
    public abstract void loadMoreItems();
}
