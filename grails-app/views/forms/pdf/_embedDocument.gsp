<%@ page import="com.eservice.eumowy.Process" %>
<figure style="width: inherit; height: 100%">
  %{--  <embed src="${pdfDocument}" type='application/pdf'
           style="width: inherit; height: 100%"
           />--}%

%{--    <object data="${pdfDocument}" type="application/pdf" style="width: inherit; height: 100%">
        <p>Your web browser doesn't have a PDF plugin.
        Instead you can <a href="${pdfDocument}">click here to
        download the PDF file.</a></p>
    </object>--}%

    <iframe src="http://docs.google.com/gview?url=http://178.239.16.28/fzs/sites/default/files/dokumenti-vijesti/sample.pdf&embedded=true"
            style="width: inherit; height: 100%" frameborder="0">
            </iframe>

</figure>

%{--
    <object data="${resource(dir:'files', file:'pedef.pdf')}" type="application/pdf">
        <p>Your web browser doesn't have a PDF plugin.
        Instead you can <a href="filename.pdf">click here to
        download the PDF file.</a></p>
    </object>--}%