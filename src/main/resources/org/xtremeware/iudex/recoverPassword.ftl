<!DOCTYPE html>
<html>
    <body style="font-family: sans-serif; font-size: 10pt;">
        <p>Hola ${userName},</p>
        <p>Hemos recibido una solicitud de recuperación de contraseña para tu cuenta.
        Para continuar por favor dirígete a:</p>
        <p>
            <a href="${appPath}/recoverPassword.xhtml?key=${key}">
                ${appPath}/recoverPassword.xhtml?key=${key}
            </a>
        </p>
    </body>
</html>