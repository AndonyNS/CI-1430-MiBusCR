json.array!(@users) do |user|
  json.extract! user, :id, :email, :encrypted_password, :nombre, :fechaNac, :ciudad
  json.url user_url(user, format: :json)
end
