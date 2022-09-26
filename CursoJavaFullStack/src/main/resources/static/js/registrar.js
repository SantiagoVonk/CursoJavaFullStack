// Call the dataTables jQuery plugin
$(document).ready(function() {
  // on fire
});

async function registrarUsuario() {
    let datos = {};
    datos.nombre = document.getElementById("txtNombre").value
    datos.apellido = document.getElementById("txtApellido").value
    datos.email = document.getElementById("txtEmail").value
    datos.password = document.getElementById("txtPassword").value

    let repetirPassword = document.getElementById("txtRepetirPassword").value

    if(repetirPassword != datos.password ) {
        alert('Ojo, la contraseña ingresada no coinciden!');
        return;
    }

      const request = await fetch('api/usuarios', {
        method: 'POST',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(datos)
      });
      alert("Su cuenta fue creada correctamente!");
      window.location.href = 'login.html'
}

