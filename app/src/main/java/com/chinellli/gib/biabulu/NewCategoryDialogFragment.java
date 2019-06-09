package com.chinellli.gib.biabulu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class NewCategoryDialogFragment extends DialogFragment {

    public static final String CATEGORY_REQUEST_KEY = "REQUEST_KEY";
    public static final String RETURN_CATEGORY_NAME_KEY = "CATEGORY_NAME_RESULT";
    public static final String UPDATE_CATEGORY_ARGUMENT_KEY = "CATEGORY_TO_EDIT_NAME";
    public static final String UPDATE_CATEGORY_POSITION_KEY = "UPDATE_CATEGORY_POSITION";
    public static final int CREATE_CATEGORY_REQUEST_CODE = 1;
    public static final int UPDATE_CATEGORY_REQUEST_CODE = 2;
    private final String create_title = "Nouvelle categorie";
    private final String update_title = "Renomer la categorie";
    private String dialogTitle;
    private String actionButtonText;
    private int requestCode;
    EditText editText;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());
        //content here
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.new_category_fragment_dialog,null);
        editText = view.findViewById(R.id.editText2);
        requestCode = (int)getArguments().get(NewCategoryDialogFragment.CATEGORY_REQUEST_KEY);
        setup(requestCode);
        builder.setTitle(dialogTitle)
                .setView(view)
                .setPositiveButton(actionButtonText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        //Toast.makeText(getActivity(), "Clicked Positive Button", Toast.LENGTH_SHORT).show();
                        //Intent intent = new Intent();
                        //sanitize textview content and the pass it as return value
                        //intent.putExtra(NewCategoryDialogFragment.RETURN_CATEGORY_NAME_KEY,editText.getText());
                        if(requestCode == CREATE_CATEGORY_REQUEST_CODE)
                            ((CategoryActionListener)getActivity()).createAction(editText.getText().toString());
                        else{
                            int position = (int)getArguments().get(NewCategoryDialogFragment.UPDATE_CATEGORY_POSITION_KEY);
                            ((CategoryActionListener)getActivity()).updateAction(position,editText.getText().toString());
                        }
                        //((CategoryActionListener)NewCategoryDialogFragment.this.getContext()).createAction(editText.getText().toString());
                        //onActivityResult(CREATE_CATEGORY_REQUEST_CODE, RESULT_OK, intent);
                    }
                })
                .setNegativeButton(R.string.cancel_category_crud_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        //Toast.makeText(getActivity(), "Clicked Negative Button", Toast.LENGTH_SHORT).show();
                        //((CategoryActionListener)getActivity()).createAction(editText.getText().toString());
                        Intent intent = new Intent();
                        intent.putExtra(NewCategoryDialogFragment.RETURN_CATEGORY_NAME_KEY,editText.getText());
                        onActivityResult(UPDATE_CATEGORY_REQUEST_CODE, RESULT_CANCELED, intent);
                    }
                });
        return builder.create();
    }

    private void setup(int requestCode){
        if(requestCode == CREATE_CATEGORY_REQUEST_CODE){
            dialogTitle = create_title;
            actionButtonText = "Creer";
        }
        else{
            dialogTitle = update_title;
            actionButtonText = "Modifier";
            editText.setText(getArguments().getCharSequence(NewCategoryDialogFragment.UPDATE_CATEGORY_ARGUMENT_KEY));
            editText.requestFocus();
        }
    }
}
