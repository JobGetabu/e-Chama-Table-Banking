package app.hacela.chamatablebanking.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import app.hacela.chamatablebanking.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class DummyTestAdapter extends RecyclerView.Adapter<DummyTestAdapter.ViewHolder> {
    private List<DummyTest> dummyTests;
    private Context context;

    public DummyTestAdapter(List<DummyTest> dummyTests, Context context) {
        this.dummyTests = dummyTests;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DummyTest dummyTest = dummyTests.get(position);

        holder.memberName.setText(dummyTest.getName());
        holder.phoneNo.setText(dummyTest.getPhone());
        holder.amountLast.setText(dummyTest.getAmount());
        holder.owingAmount.setText(dummyTest.getOwing());

        Picasso.get().load("https://www.gravatar.com/avatar/205e460b479e2e5b48aec07710c08d50")
                .placeholder(R.drawable.avatar_placeholder).into(holder.memberImage);
    }

    @Override
    public int getItemCount() {
        return dummyTests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView memberImage;
        public TextView memberName, phoneNo, amountLast, owingAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            memberImage = itemView.findViewById(R.id.member_profile_pic);
            memberName = itemView.findViewById(R.id.member_name);
            phoneNo = itemView.findViewById(R.id.phone_number);
            amountLast = itemView.findViewById(R.id.last_contribution);
            owingAmount = itemView.findViewById(R.id.amount_owing);
        }
    }
}
