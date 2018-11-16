package com.pait.exec.adapters;

//Created by SNEHA on 10/27/2017.

public class TrackOrderDetailAdapter {
/*

    private HashMap<Integer,List<TrackOrderDetailClass>> map_child;
    private HashMap<Integer,String> map_parent;
    private Context context;
    List<Integer> prodIdList;

    public TrackOrderDetailAdapter(HashMap<Integer,String> _map_parent,HashMap<Integer,List<TrackOrderDetailClass>> _map_child, List<Integer> _prodIdList,Context _context){
        this.context = _context;
        this.map_parent = _map_parent;
        this.map_child = _map_child;
        this.prodIdList = _prodIdList;
    }

    @Override
    public int getGroupCount() {
        return map_parent.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return map_child.get(prodIdList.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return map_parent.get(prodIdList.get(i));
    }

    @Override
    public Object getChild(int i, int i1) {
        return map_child.get(prodIdList.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View convertView, ViewGroup viewGroup) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_track_order_detail_parent,null);
        }
        String finalProd = (String) getGroup(i);
        TextView tv_finalprod = (TextView) convertView.findViewById(R.id.tv_finalprod);
        tv_finalprod.setText(finalProd);
        ExpandableListView mExpandableListView = (ExpandableListView) viewGroup;
        mExpandableListView.expandGroup(i);
        return convertView;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View convertView, ViewGroup viewGroup) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_track_order_detail_child,null);
        }
        TrackOrderDetailClass detClass = (TrackOrderDetailClass) getChild(i,i1);
        TextView tv_sizegroup = (TextView) convertView.findViewById(R.id.tv_sizegroup);
        TextView tv_color = (TextView) convertView.findViewById(R.id.tv_color);
        TextView tv_mrp = (TextView) convertView.findViewById(R.id.tv_mrp);
        TextView tv_qty = (TextView) convertView.findViewById(R.id.tv_qty);
        TextView tv_loosepacktyp = (TextView) convertView.findViewById(R.id.tv_loosepacktyp);

        tv_sizegroup.setText(detClass.getSize_group());
        tv_color.setText(detClass.getColor());
        tv_mrp.setText(detClass.getMrp());
        tv_qty.setText(detClass.getActLooseQty());
        tv_loosepacktyp.setText(detClass.getLoosePackTyp());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
*/
}
