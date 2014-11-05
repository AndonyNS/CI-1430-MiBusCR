class User < ActiveRecord::Base
 has_many :ruta_user
 has_many :ruta, through: :ruta_user

 before_create :generate_token
 EMAIL_REGEX = /\A([^@\s]+)@((?:[-a-z0-9]+\.)+[a-z]{2,})\z/i
 validates :nombre, :presence => true, :length => { :in => 3..20 }
 validates :email, :presence => true, :uniqueness => true, :format => EMAIL_REGEX

  private
    def generate_token
      begin
        self.token = SecureRandom.hex.to_s
      end while self.class.exists?(token: token)
    end
end
