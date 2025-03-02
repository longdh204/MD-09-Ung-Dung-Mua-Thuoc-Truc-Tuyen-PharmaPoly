//package com.md09.pharmapoly.Adapters;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.md09.pharmapoly.R;
//import com.md09.pharmapoly.Models.Section;
//
//import java.util.List;
//
//public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.ViewHolder> {
//
//    private Context context;
//    private List<Section> sectionList;
//
//    public SectionAdapter(Context context, List<Section> sectionList) {
//        this.context = context;
//        this.sectionList = sectionList;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.test, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Section section = sectionList.get(position);
//
//        holder.sectionTitle.setText(section.getDetails().get(0).getTitle());  // Tiêu đề của phần
//        holder.sectionContent.setText(section.getDetails().get(0).getContent());  // Nội dung của phần
//    }
//
//    @Override
//    public int getItemCount() {
//        return sectionList.size();
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        TextView sectionTitle, sectionContent;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            sectionTitle = itemView.findViewById(R.id.sectionTitle);
//            sectionContent = itemView.findViewById(R.id.sectionContent);
//        }
//    }
//}
