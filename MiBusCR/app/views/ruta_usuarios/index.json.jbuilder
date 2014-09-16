json.array!(@ruta_usuarios) do |ruta_usuario|
  json.extract! ruta_usuario, :id, :ruta_id, :user_id
  json.url ruta_usuario_url(ruta_usuario, format: :json)
end
