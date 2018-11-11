package app.hacela.chamatablebanking.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ramotion.foldingcell.FoldingCell;
import com.squareup.picasso.Picasso;

import java.util.List;

import app.hacela.chamatablebanking.R;
import butterknife.ButterKnife;
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

        LayoutInflater vi = LayoutInflater.from(context);
        final FoldingCell cell = (FoldingCell) vi.inflate(R.layout.single_transaction, parent, false);

        // attach click listener to folding cell
        cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cell.toggle(false);
            }
        });


        return new ViewHolder(cell);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        DummyTest dummyTest = dummyTests.get(position);

        holder.memberName.setText(dummyTest.getName());
        holder.phoneNo.setText(dummyTest.getPhone());
        holder.amountLast.setText(dummyTest.getAmount());
        holder.owingAmount.setText(dummyTest.getOwing());
        holder.memberNameExpanded.setText(dummyTest.getName());
        holder.phoneNoExpanded.setText(dummyTest.getPhone());
        holder.memberImage.setImageResource(dummyTest.getPhoto());
        holder.memberImageExpanded.setImageResource(dummyTest.getPhoto());
        holder.phone_desc.setText(dummyTest.getPhone_desc());
        holder.phone_desc_expanded.setText(dummyTest.getPhone_desc());
        holder.contrib_desc.setText(dummyTest.getContribs_desc());
        holder.amount_desc.setText(dummyTest.getAmount_desc());
        holder.popup_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, holder.popup_menu);
                popupMenu.inflate(R.menu.card_view_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.add_profit:
                                Toast.makeText(context, "Profit Added Successfully", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.add_expense:
                                Toast.makeText(context, "Expense added Successfully", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.delete_project:
                                Toast.makeText(context, "Project Deleted Successfully", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return dummyTests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView memberImage, memberImageExpanded;
        public TextView memberName, phoneNo, phone_desc, phone_desc_expanded, amountLast, amount_desc, owingAmount, contrib_desc, memberNameExpanded, phoneNoExpanded;
        public ImageView popup_menu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            memberImage = itemView.findViewById(R.id.member_profile_pic);
            memberImageExpanded = itemView.findViewById(R.id.member_profile_pic_expanded);
            memberName = itemView.findViewById(R.id.member_name);
            memberNameExpanded = itemView.findViewById(R.id.member_name_expanded);
            phoneNo = itemView.findViewById(R.id.phone_number);
            phoneNoExpanded = itemView.findViewById(R.id.member_phone_expanded);
            amountLast = itemView.findViewById(R.id.last_contribution);
            owingAmount = itemView.findViewById(R.id.amount_owing);
            phone_desc = itemView.findViewById(R.id.phone_desc);
            phone_desc_expanded = itemView.findViewById(R.id.phone_desc_expanded);
            amount_desc = itemView.findViewById(R.id.totals_desc);
            contrib_desc = itemView.findViewById(R.id.contrib_desc);
            popup_menu = itemView.findViewById(R.id.fc_menu);

        }
    }

}
