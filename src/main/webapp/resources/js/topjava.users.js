var userAjaxUrl = "admin/users/";

// $(document).ready(function () {
$(function () {
    makeEditable({
            ajaxUrl: userAjaxUrl,
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "name"
                    },
                    {
                        "data": "email"
                    },
                    {
                        "data": "roles"
                    },
                    {
                        "data": "enabled"
                    },
                    {
                        "data": "registered"
                    },
                    {
                        "defaultContent": "Edit",
                        "orderable": false
                    },
                    {
                        "defaultContent": "Delete",
                        "orderable": false
                    }
                ],
                "order": [
                    [
                        0,
                        "asc"
                    ]
                ]
            })
        }
    );

    $('input:checked').each(function () {
        $(this).closest("tr").attr('data-userEnabled', true);
    })

    $('input').on('click', function () {
        //     var id = $(this).closest("tr").attr("id");
        //     var enabled = chkbox.is(":checked");
        //     $.ajax({
        //         url: userAjaxUrl + id,
        //         type: "POST",
        //         data: ??
        // }
        if ($(this).is(':checked')) {
            $(this).closest("tr").attr('data-userEnabled', true);
        } else {
            $(this).closest("tr").attr('data-userEnabled', false);
        }
    })
});