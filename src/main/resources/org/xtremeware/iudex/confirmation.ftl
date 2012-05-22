<!DOCTYPE html>
<html>
    <body style="font-family: sans-serif; font-size: 10pt;">
        <p>Hola ${userName},</p>
        <p>Tu registro ha sido exitoso. Antes de iniciar sesión debes confirmar
            tu dirección de correo electrónico, para ello dirígete por favor a:</p>
        <p>
            <a href="${appPath}/confirm.xhtml?key=${confirmationKey}">
                ${appPath}/confirm.xhtml?key=${confirmationKey}
            </a>
        </p>
    </body>
</html>